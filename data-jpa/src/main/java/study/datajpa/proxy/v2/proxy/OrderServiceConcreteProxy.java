package study.datajpa.proxy.v2.proxy;

import lombok.RequiredArgsConstructor;
import study.datajpa.advanced.trace.LogTrace;
import study.datajpa.advanced.trace.TraceStatus;
import study.datajpa.proxy.v2.OrderServiceProxyV2;


public class OrderServiceConcreteProxy extends OrderServiceProxyV2 {
    private final OrderServiceProxyV2 target;
    private final LogTrace logTrace;

    public OrderServiceConcreteProxy(OrderServiceProxyV2 target, LogTrace logTrace){
        super(null); //부모꺼를 안쓸꺼라서 null로 넣는다.
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;

        try{
            status = logTrace.begin("OrderService.orderItem()");
            target.orderItem(itemId);
            logTrace.end(status);
        }catch (Exception e){
            logTrace.exception(status,e);
            throw e;
        }
    }
}
