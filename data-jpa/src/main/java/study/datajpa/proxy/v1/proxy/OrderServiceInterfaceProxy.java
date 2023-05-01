package study.datajpa.proxy.v1.proxy;

import lombok.RequiredArgsConstructor;
import study.datajpa.advanced.trace.LogTrace;
import study.datajpa.advanced.trace.TraceStatus;
import study.datajpa.proxy.v1.OrderRepositoryProxyV1;
import study.datajpa.proxy.v1.OrderServiceProxyV1;

@RequiredArgsConstructor
public class OrderServiceInterfaceProxy implements OrderServiceProxyV1 {

    private final OrderServiceProxyV1 target;
    private final LogTrace logTrace;

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;

        try{
            status = logTrace.begin("OrderServoce.orderItem()");
            target.orderItem(itemId);
            logTrace.end(status);
        }catch (Exception e){
            logTrace.exception(status,e);
            throw e;
        }
    }
}
