package personal.czg.rpc.fault;

import cn.hutool.http.HttpResponse;
import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;
import personal.czg.rpc.model.RpcResponse;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FixedIntervalRetry implements Retry{
    @Override
    public HttpResponse doRetry(Callable<HttpResponse> callable) throws ExecutionException, RetryException {
        Retryer<HttpResponse> retryer = RetryerBuilder.<HttpResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class) //出现某个意外时重试
                .withWaitStrategy(WaitStrategies.fixedWait(1L, TimeUnit.SECONDS)) //重试间隔时间
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))  //停止策略
                .withRetryListener(new RetryListener() {                            // 每次重试后执行
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("重试次数 {}", attempt.getAttemptNumber());
                    }
                })
                .build();
        return retryer.call(callable);
    }
}
