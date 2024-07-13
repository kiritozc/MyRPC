package personal.czg.rpc.registry;

import cn.hutool.json.JSONUtil;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import lombok.extern.slf4j.Slf4j;
import personal.czg.rpc.config.RegistryConfig;
import personal.czg.rpc.model.ServiceMetaInfo;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class EtcdRegistry implements Registry{
    private Client client;
    private KV kvClient;

    /**
     *根节点
     */
    private static final String ETCD_ROOT_PATH= "/META-INF/rpc/";
    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();

    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        //设置要存储的键值对,key要包含地址，因为可能有多个同名的服务，只有地址是唯一的
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // 获取一个设置对象，设置过期时间（租约）为30s
        Lease leaseClient = client.getLeaseClient();
        long leaseId = leaseClient.grant(300).get().getID();
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();

        // 存储
        kvClient.put(key, value, putOption).get();
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        kvClient.delete(ByteSequence.from(ETCD_ROOT_PATH+serviceMetaInfo.getServiceNodeKey(),StandardCharsets.UTF_8));
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        // 前缀搜索，一定要加“/”
        String preKey = ETCD_ROOT_PATH + serviceKey + "/";

        try{
            // 前缀查询设置
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            // 查询
            List<KeyValue> kvs = kvClient.get(ByteSequence.from(preKey, StandardCharsets.UTF_8), getOption)
                    .get().getKvs();

            // 将服务信息的value取出来转为ServiceMetaInfo类型.
            return kvs.stream().map(keyValue -> {
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败",e);
        }
    }

    /**
     * 注册中心销毁
     */
    @Override
    public void destroy() {
        //释放资源
        if(kvClient != null){
            kvClient.close();
        }
        if(client != null){
            client.close();
        }
        log.info("节点{}已下线",this.toString());
    }

    @Override
    public void put(String key, String serviceName){
        kvClient.put(ByteSequence.from(key, StandardCharsets.UTF_8), ByteSequence.from(serviceName, StandardCharsets.UTF_8));
    }
}
