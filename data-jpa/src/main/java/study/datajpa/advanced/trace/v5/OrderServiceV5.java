package study.datajpa.advanced.trace.v5;

import org.springframework.stereotype.Service;
import study.datajpa.advanced.trace.LogTrace;
import study.datajpa.advanced.trace.callback.TraceTemplate;

@Service
public class OrderServiceV5 {

    private final OrderRepositoryV5 orderRepository;
    private final TraceTemplate template;

    public OrderServiceV5(OrderRepositoryV5 orderRepositoryV, LogTrace trace) {
        this.orderRepository = orderRepositoryV;
        this.template = new TraceTemplate(trace);
    }

    public void orderItem(String itemId){

        template.execute("OrderService0.orderItem()", () -> {
            orderRepository.save(itemId);
            return null;
        });

    }
}
