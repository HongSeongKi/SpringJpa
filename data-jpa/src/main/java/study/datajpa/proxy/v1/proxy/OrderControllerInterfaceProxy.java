package study.datajpa.proxy.v1.proxy;

import lombok.RequiredArgsConstructor;
import study.datajpa.advanced.trace.LogTrace;
import study.datajpa.advanced.trace.TraceStatus;
import study.datajpa.proxy.v1.OrderControllerProxyV1;

@RequiredArgsConstructor
public class OrderControllerInterfaceProxy implements OrderControllerProxyV1 {
    private final OrderControllerProxyV1 target;
    private final LogTrace logTrace;

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
