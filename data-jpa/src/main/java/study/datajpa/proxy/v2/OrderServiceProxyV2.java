package study.datajpa.proxy.v2;


import lombok.extern.slf4j.Slf4j;


@Slf4j
public class OrderServiceProxyV2 {
    private final OrderRepositoryProxyV2 orderRepositoryProxy;

    public OrderServiceProxyV2(OrderRepositoryProxyV2 orderRepositoryProxy){
        this.orderRepositoryProxy = orderRepositoryProxy;
    }

    public void orderItem(String itemId) {
        orderRepositoryProxy.save(itemId);
    }
}
