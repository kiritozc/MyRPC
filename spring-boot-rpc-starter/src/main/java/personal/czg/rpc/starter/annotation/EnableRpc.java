package personal.czg.rpc.starter.annotation;

import org.springframework.context.annotation.Import;
import personal.czg.rpc.starter.bootstrap.EnableBootStrap;
import personal.czg.rpc.starter.bootstrap.RpcConsumerBootstrap;
import personal.czg.rpc.starter.bootstrap.RpcProviderBootstrap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({EnableBootStrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {

    /**
     * 是否需要启动 server
     */
    boolean needServer() default true;
}
