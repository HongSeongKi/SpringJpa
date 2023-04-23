package study.datajpa.advanced.trace.v2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.datajpa.advanced.trace.TraceId;
import study.datajpa.advanced.trace.TraceStatus;
import study.datajpa.advanced.trace.hellotrace.HelloTraceV1;
import study.datajpa.advanced.trace.hellotrace.HelloTraceV2;

@Service
@RequiredArgsConstructor
public class OrderServiceV2 {

    private final OrderRepositoryV2 orderRepositoryV1;
    private final HelloTraceV2 trace;

    public void orderItem(TraceId traceId, String itemId){
        TraceStatus status = null;
        try{
            status = trace.beginSync(traceId,"OrderServiceV1.orderItem()");
            orderRepositoryV1.save(traceId,itemId);
            trace.end(status);
        }catch (Exception e){
            trace.exception(status,e);
            throw e;
        }

    }
}
