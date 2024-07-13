package personal.czg;

import cn.hutool.json.JSONUtil;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Watch;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.watch.WatchEvent;
import personal.czg.rpc.RpcApplication;
import personal.czg.rpc.config.RpcConfig;
import personal.czg.rpc.model.ServiceMetaInfo;
import personal.czg.rpc.registry.Registry;
import personal.czg.rpc.registry.RegistryFactory;

import java.nio.charset.StandardCharsets;


public class WatchKeyTest {

    public static void main(String[] args) throws Exception {
        Client client = Client.builder().endpoints("http://localhost:2379").build();
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        // 创建并启动监听线程
        Thread watchThread = new Thread(() -> {
            Watch watch = client.getWatchClient();
            ByteSequence key = ByteSequence.from("wrong:localhost", StandardCharsets.UTF_8);
            WatchOption watchOption = WatchOption.newBuilder().withPrefix(key).build();

            Watch.Watcher watcher = watch.watch(key, watchOption, response -> {
                for (WatchEvent event : response.getEvents()) {
                    String foundKey = event.getKeyValue().getKey().toString();
                    System.out.println("重建服务: " + foundKey);
                    String value = event.getKeyValue().getValue().toString(StandardCharsets.UTF_8);
                    System.out.println(value);
                    ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                    try {
                        System.out.println("开始重建");
                        registry.register(serviceMetaInfo);
                        client.getKVClient().delete(event.getKeyValue().getKey());
                        System.out.println("重建完成");
                    } catch (Exception e) {
                        System.out.println("重建失败");
                        throw new RuntimeException(e);
                    }
                }
            });
            // 阻塞当前线程，保持监听
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 关闭watcher
            watcher.close();
        });
        watchThread.start();

    }
}

