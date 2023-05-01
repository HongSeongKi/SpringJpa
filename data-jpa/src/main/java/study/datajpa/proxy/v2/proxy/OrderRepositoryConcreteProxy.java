package study.datajpa.proxy.v2.proxy;

import study.datajpa.advanced.trace.LogTrace;
import study.datajpa.advanced.trace.TraceStatus;
import study.datajpa.proxy.v2.OrderRepositoryProxyV2;

public class OrderRepositoryConcreteProxy extends OrderRepositoryProxyV2 {

    private OrderRepositoryProxyV2 target;
    private LogTrace logTrace;

    public OrderRepositoryConcreteProxy(OrderRepositoryProxyV2 target, LogTrace logTrace) {
        this.target = target;
        this.logTrace = logTrace;
    }

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
