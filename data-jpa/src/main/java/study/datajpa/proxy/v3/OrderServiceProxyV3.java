package study.datajpa.proxy.v3;

import org.springframework.stereotype.Service;
import study.datajpa.proxy.v2.OrderRepositoryProxyV2;

@Service
public class OrderServiceProxyV3 {
    private final OrderRepositoryProxyV3 orderRepositoryProxy;

    public OrderServiceProxyV3(OrderRepositoryProxyV3 orderRepositoryProxy){
        this.orderRepositoryProxy = orderRepositoryProxy;
    }

    public void orderItem(String itemId) {
        orderRepositoryProxy.save(itemId);
    }
}
