package personal.czg.rpc.fault;

import cn.hutool.http.HttpResponse;
import com.github.rholder.retry.RetryException;
import personal.czg.rpc.model.RpcResponse;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class NoRetry implements Retry{
    @Override
    public HttpResponse doRetry(Callable<HttpResponse> callable) throws Exception {
        return callable.call();
    }
}
