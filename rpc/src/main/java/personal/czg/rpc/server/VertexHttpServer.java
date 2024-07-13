package personal.czg.rpc.server;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpVersion;

import java.util.Collections;

public class VertexHttpServer implements HttpServer{
    @Override
    public void serverStart(int port) {
        HttpServerOptions options = new HttpServerOptions();
        // 实例化vertx
        Vertx vertex = Vertx.vertx();
        // 创建http服务器
        io.vertx.core.http.HttpServer httpServer = vertex.createHttpServer();
        // 监听端口并处理请求
        httpServer.requestHandler(new HttpServerHandler());
        System.out.println("新一次监听");
        // 启动HTTP服务器并监听端口
        httpServer.listen(port, result->{
            if(result.succeeded()){
                System.out.println("正在监听端口："+port);
            }else{
                System.err.println("服务开启失败");
            }
        });

    }
}
