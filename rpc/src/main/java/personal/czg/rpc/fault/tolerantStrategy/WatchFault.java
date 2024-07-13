package personal.czg.rpc.fault.tolerantStrategy;

import cn.hutool.json.JSONUtil;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.WatchOption;
import io.etcd.jetcd.watch.WatchEvent;
import personal.czg.rpc.RpcApplication;
import personal.czg.rpc.config.RpcConfig;
import personal.czg.rpc.model.ServiceMetaInfo;
import personal.czg.rpc.registry.Registry;
import personal.czg.rpc.registry.RegistryFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WatchFault {
    public static void startWatch() throws IOException {
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
                    ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                    try {
                        registry.register(serviceMetaInfo);
                    } catch (Exception e) {
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

//        // 主线程等待用户输入后关闭监听线程和client
        System.out.println("Press any key to stop watching...");
        System.in.read();

        // 关闭watchThread和client
        watchThread.interrupt();
        client.close();
    }
}
