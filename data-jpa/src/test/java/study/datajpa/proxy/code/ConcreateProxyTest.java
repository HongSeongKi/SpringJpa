package study.datajpa.proxy.code;

import org.junit.jupiter.api.Test;
import study.datajpa.proxy.v1.proxy.ConcreateClient;
import study.datajpa.proxy.v1.proxy.ConcreateLogic;

public class ConcreateProxyTest {

    @Test
    void noProxy(){
        ConcreateLogic logic = new ConcreateLogic();
        ConcreateClient client = new ConcreateClient(logic);
        client.execute();
    }

    @Test
    void addProxy(){
        ConcreateLogic logic = new ConcreateLogic();
        TimeProxy proxy = new TimeProxy(logic);
        ConcreateClient client = new ConcreateClient(proxy);
        client.execute();
    }
}
