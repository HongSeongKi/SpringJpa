package study.datajpa.advanced.trace.v5;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.advanced.trace.LogTrace;
import study.datajpa.advanced.trace.callback.TraceCallback;
import study.datajpa.advanced.trace.callback.TraceTemplate;
import study.datajpa.advanced.trace.template.AbstractTemplate;

@Repository
public class OrderRepositoryV5 {

    private final TraceTemplate template;

    public OrderRepositoryV5(LogTrace trace){
        template = new TraceTemplate(trace);
    }

    public void save(String itemId){

        template.execute("OrderRepository.save()", () -> {
            if(itemId.equals("ex")){
                throw new IllegalStateException("예외 발생");
            }
            sleep(1000);
            return null;
        });

    }

    private void sleep(int millis){
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
