package study.datajpa.proxy.v2.proxy;

import study.datajpa.advanced.trace.LogTrace;
import study.datajpa.advanced.trace.TraceStatus;
import study.datajpa.proxy.v2.OrderControllerProxyV2;

public class OrderControllerConcreteProxy extends OrderControllerProxyV2 {

    private final OrderControllerProxyV2 target;
    private final LogTrace logTrace;

    public OrderControllerConcreteProxy(OrderControllerProxyV2 target, LogTrace logTrace){
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try{
            status = logTrace.begin("OrderController.request()");
            String result = target.request(itemId);
            logTrace.end(status);
            return result;
        }catch (Exception e){
            logTrace.exception(status,e);
            throw e;
        }
    }

    @Override
    public String noLog() {
        return target.noLog();
    }
}
