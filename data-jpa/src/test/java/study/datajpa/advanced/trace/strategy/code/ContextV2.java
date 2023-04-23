package study.datajpa.advanced.trace.strategy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextV2 {

    public void execute(Strategy strategy) {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        strategy.call();
        long endTime = System.currentTimeMillis();
        long result = endTime - startTime;
        log.info("resulttime : "+result);
    }

}
