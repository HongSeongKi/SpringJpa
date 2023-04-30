package study.datajpa.advanced.trace.strategy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextV1 { //변하지 않는 부분 사실상 템플릿역할

    private Strategy strategy;

    public ContextV1(Strategy strategy){
        this.strategy = strategy;
    }

    public void execute() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        strategy.call();
        long endTime = System.currentTimeMillis();
        long result = endTime - startTime;
        log.info("resulttime : "+result);
    }

}
