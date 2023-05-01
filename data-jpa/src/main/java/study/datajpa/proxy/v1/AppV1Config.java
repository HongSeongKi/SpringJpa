package study.datajpa.proxy.v1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//수동 등록
public class AppV1Config {

    @Bean
    public OrderControllerProxyV1 orderControllerProxyV1() {
        return new OrderControllerProxyV1Impl(OrderServiceProxyV1());
    }

    @Bean
    public OrderServiceProxyV1 OrderServiceProxyV1() {
        return new OrderServiceProxyV1Impl(OrderRepositoryProxyV1());
    }

    @Bean
    public OrderRepositoryProxyV1 OrderRepositoryProxyV1() {
        return new OrderRepositoryProxyV1Impl();
    }
}
