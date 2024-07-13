package personal.czg.rpc.fault;

import cn.hutool.http.HttpResponse;
import com.github.rholder.retry.RetryException;
import personal.czg.rpc.model.RpcResponse;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public interface Retry {

    /**
     * Callable是一个可调用的方法
     * 其中，泛型的作用是指定这个方法的返回值类型
     * @param callable
     * @return
     */
    HttpResponse doRetry(Callable<HttpResponse> callable) throws Exception;
}
