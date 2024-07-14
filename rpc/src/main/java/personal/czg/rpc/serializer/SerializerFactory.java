package personal.czg.rpc.serializer;

import personal.czg.rpc.spi.SpiLoader;

public class SerializerFactory {
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认使用HessianSerializer
     */
    private static final Serializer DEFAULT_SERIALIZER = new HessianSerializer();

    /**
     * 获取序列化器实例
     */
    public static Serializer getInstance(String key){
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
