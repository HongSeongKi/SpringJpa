package study.datajpa.advanced.trace.v4;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.datajpa.advanced.trace.LogTrace;
import study.datajpa.advanced.trace.TraceStatus;
import study.datajpa.advanced.trace.template.AbstractTemplate;

@Service
@RequiredArgsConstructor
public class OrderServiceV4 {

    private final OrderRepositoryV4 orderRepositoryV;
    private final LogTrace trace;

    public void orderItem( String itemId){
        AbstractTemplate<Void> template = new AbstractTemplate<>(trace) {
            @Override
            public Void call() {
                orderRepositoryV.save(itemId);
                return null;
            }
        };
        template.execute("OrderService0.orderItem()");
    }
}
