package study.datajpa.proxy.v1;

public class OrderServiceProxyV1Impl implements OrderServiceProxyV1 {

    private final OrderRepositoryProxyV1 orderRepositoryProxyV1;

    public OrderServiceProxyV1Impl(OrderRepositoryProxyV1 orderRepositoryProxyV1){
        this.orderRepositoryProxyV1 = orderRepositoryProxyV1;
    }

    @Override
    public void orderItem(String itemId) {
        orderRepositoryProxyV1.save(itemId);
    }
}
