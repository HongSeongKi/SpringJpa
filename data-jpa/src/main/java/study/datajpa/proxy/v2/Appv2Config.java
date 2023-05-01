package study.datajpa.proxy.v2;

import org.springframework.context.annotation.Bean;


public class Appv2Config {

    @Bean
    public OrderControllerProxyV2 orderControllerProxyV2() {
        return new OrderControllerProxyV2(OrderServiceProxyV2());
    }

    @Bean
    public OrderServiceProxyV2 OrderServiceProxyV2() {
        return new OrderServiceProxyV2(OrderRepositoryProxyV2());
    }

    @Bean
    public OrderRepositoryProxyV2 OrderRepositoryProxyV2() {
        return new OrderRepositoryProxyV2();
    }
}
