package personal.czg.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRegisterInfo<T> {
    private String serviceName;
    private Class<? extends T> ImplClass;
}
