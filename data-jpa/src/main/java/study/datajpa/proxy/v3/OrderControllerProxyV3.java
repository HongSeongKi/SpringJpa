package study.datajpa.proxy.v3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class OrderControllerProxyV3 {
    private final OrderServiceProxyV3 orderServiceProxy;

    public OrderControllerProxyV3(OrderServiceProxyV3 orderServiceProxy) {
        this.orderServiceProxy = orderServiceProxy;
    }

    @GetMapping("/proxy/v3/request")
    public String request(String itemId) {
        orderServiceProxy.orderItem(itemId);
        return "ok";
    }

    @GetMapping("/proxy/v3/no-log")
    public String noLog() {
        return "ok";
    }
}
