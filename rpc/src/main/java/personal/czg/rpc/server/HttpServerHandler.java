package personal.czg.rpc.server;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.slf4j.Slf4j;
import personal.czg.rpc.RpcApplication;
import personal.czg.rpc.model.RpcRequest;
import personal.czg.rpc.model.RpcResponse;
import personal.czg.rpc.registry.LocalRegistry;
import personal.czg.rpc.serializer.JdkSerializer;
import personal.czg.rpc.serializer.Serializer;
import personal.czg.rpc.serializer.SerializerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 1、反序列化请求为对象，获取请求参数；
 * 2、根据参数获取服务的实现类；
 * 3、通过反射调用方法，获取服务结果；
 * 4、序列化结果，写入响应中。
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest httpServerRequest) {
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        System.out.println("收到请求："+httpServerRequest.method()+" "+httpServerRequest.uri());
        // 异步处理HTTP请求
        httpServerRequest.bodyHandler(body->{
            // 反序列化请求
           byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try{
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e){
                e.printStackTrace();
            }
            // 构造请求对象
            RpcResponse rpcResponse = new RpcResponse();
            if(rpcRequest == null){
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(httpServerRequest,rpcResponse,serializer);
                return;
            }
            try{
                // 获取要调用的服务类和方法
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                // 获取结果
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                // 封装结果
                rpcResponse.setMessage("ok");
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            doResponse(httpServerRequest, rpcResponse, serializer);

        });
    }

    private void doResponse(HttpServerRequest httpServerRequest, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = httpServerRequest.response().putHeader("content-type", "application/json");
        try{
            byte[] serialize = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialize));
        } catch (Exception e){
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }


}
