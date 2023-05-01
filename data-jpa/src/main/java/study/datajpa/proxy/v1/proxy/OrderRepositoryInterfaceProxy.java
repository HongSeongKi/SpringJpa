package study.datajpa.proxy.v1.proxy;

import lombok.RequiredArgsConstructor;
import study.datajpa.advanced.trace.LogTrace;
import study.datajpa.advanced.trace.TraceStatus;
import study.datajpa.proxy.v1.OrderRepositoryProxyV1;

@RequiredArgsConstructor
public class OrderRepositoryInterfaceProxy implements OrderRepositoryProxyV1 {

    private final OrderRepositoryProxyV1 target;
    private final LogTrace logTrace;


    @Override
    public void save(String itemId) {
        TraceStatus status = null;

        try{
            status = logTrace.begin("OrderRepository.request()");
            target.save(itemId);
            logTrace.end(status);
        }catch (Exception e){
            logTrace.exception(status,e);
            throw e;
        }
    }
}
