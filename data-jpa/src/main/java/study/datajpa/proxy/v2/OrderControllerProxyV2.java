package study.datajpa.proxy.v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import study.datajpa.proxy.v1.OrderServiceProxyV1;

@Slf4j
@RequestMapping
@ResponseBody
public class OrderControllerProxyV2 {
    private final OrderServiceProxyV2 orderServiceProxy;

    public OrderControllerProxyV2(OrderServiceProxyV2 orderServiceProxy) {
        this.orderServiceProxy = orderServiceProxy;
    }

    @GetMapping("/proxy/v2/request")
    public String request(String itemId) {
        orderServiceProxy.orderItem(itemId);
        return "ok";
    }

    @GetMapping("/proxy/v2/no-log")
    public String noLog() {
        return "ok";
    }
}
