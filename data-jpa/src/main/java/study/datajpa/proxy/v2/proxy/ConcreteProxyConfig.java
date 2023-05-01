package study.datajpa.proxy.v2.proxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.datajpa.advanced.trace.LogTrace;
import study.datajpa.proxy.v2.OrderControllerProxyV2;
import study.datajpa.proxy.v2.OrderRepositoryProxyV2;
import study.datajpa.proxy.v2.OrderServiceProxyV2;

@Configuration
public class ConcreteProxyConfig {

    @Bean
    public OrderControllerProxyV2 orderControllerProxyV2(LogTrace logTrace){
        OrderControllerProxyV2 controllerImpl = new OrderControllerProxyV2(orderServiceProxyV2(logTrace));
        return new OrderControllerConcreteProxy(controllerImpl,logTrace);
    }

    @Bean
    public OrderServiceProxyV2 orderServiceProxyV2(LogTrace logTrace){
        OrderServiceProxyV2 orderServiceProxyV2 = new OrderServiceProxyV2(orderRepositoryProxyV2(logTrace));
        return new OrderServiceConcreteProxy(orderServiceProxyV2,logTrace);
    }

    @Bean
    public OrderRepositoryProxyV2 orderRepositoryProxyV2(LogTrace logTrace){
        OrderRepositoryProxyV2 repositoryImpl = new OrderRepositoryProxyV2();
        return new OrderRepositoryConcreteProxy(repositoryImpl, logTrace);

    }
}
