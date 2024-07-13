package personal.czg.rpc.fault;

import personal.czg.rpc.spi.SpiLoader;

public class RetryFactory {
    static {
        SpiLoader.load(Retry.class);
    }

    private static final Retry DEFAULT_RETRY = new NoRetry();

    public static Retry getInstance(String key){
        return SpiLoader.getInstance(Retry.class, key);
    }
}
