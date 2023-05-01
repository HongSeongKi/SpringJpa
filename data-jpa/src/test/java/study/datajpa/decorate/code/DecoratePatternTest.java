package study.datajpa.decorate.code;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DecoratePatternTest {

    @Test
    void noDecorator(){
        Component realComponent = new RealComponent();
        DecoratePatternClient client = new DecoratePatternClient(realComponent);
        client.execute();
    }

    @Test
    void decorator1(){
        Component realComponent = new RealComponent();
        MessageDecorator messageDecorator = new MessageDecorator(realComponent);
        DecoratePatternClient client = new DecoratePatternClient(messageDecorator);
        client.execute();
    }

    @Test
    void decorator2(){
        Component realComponent = new RealComponent();
        MessageDecorator messageDecorator = new MessageDecorator(realComponent);
        TimeDecorator timeDecorator = new TimeDecorator(messageDecorator);
        DecoratePatternClient client = new DecoratePatternClient(timeDecorator);
        client.execute();
    }
}
