package study.datajpa.proxy.v1.proxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.datajpa.advanced.trace.LogTrace;
import study.datajpa.proxy.v1.*;

@Configuration
public class InterfaceProxyCofig {

    @Bean
    public OrderControllerProxyV1 orderController(LogTrace logTrace) {
        OrderControllerProxyV1Impl controllerImpl = new OrderControllerProxyV1Impl(OrderService(logTrace));
        return new OrderControllerInterfaceProxy(controllerImpl,logTrace);
    }

    @Bean
    public OrderServiceProxyV1 OrderService(LogTrace logTrace){
        OrderServiceProxyV1Impl serviceImpl = new OrderServiceProxyV1Impl(OrderRepository(logTrace));
        return new OrderServiceInterfaceProxy(serviceImpl,logTrace);
    }

    @Bean
    public OrderRepositoryProxyV1 OrderRepository(LogTrace logTrace) {
        OrderRepositoryProxyV1Impl repositoryImpl = new OrderRepositoryProxyV1Impl();
        return new OrderRepositoryInterfaceProxy(repositoryImpl,logTrace);
    }
}
