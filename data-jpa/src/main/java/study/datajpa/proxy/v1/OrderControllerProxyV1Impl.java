package study.datajpa.proxy.v1;

public class OrderControllerProxyV1Impl implements OrderControllerProxyV1{

    private final OrderServiceProxyV1 orderServiceProxyV1;

    public OrderControllerProxyV1Impl(OrderServiceProxyV1 orderServiceProxyV1) {
        this.orderServiceProxyV1 = orderServiceProxyV1;
    }

    @Override
    public String request(String itemId) {
        orderServiceProxyV1.orderItem(itemId);
        return "ok";
    }

    @Override
    public String noLog() {
        return "ok";
    }
}
