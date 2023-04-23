package study.datajpa.advanced.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import study.datajpa.advanced.trace.strategy.code.ContextV2;
import study.datajpa.advanced.trace.strategy.code.Strategy;
import study.datajpa.advanced.trace.strategy.code.StrategyLogic1;
import study.datajpa.advanced.trace.strategy.code.StrategyLogic2;

@Slf4j
public class ContextV2Test {

    /**
     * 전략 패턴 적용
     */
    @Test
    void strategyV1(){
        ContextV2 context = new ContextV2(); // 실행시점에 전략을 넣어서 실행시킬 수 있다.
        context.execute(new StrategyLogic1());
        context.execute(new StrategyLogic2());
    }

    @Test
    void strategyV2(){
        ContextV2 context = new ContextV2(); // 실행시점에 전략을 넣어서 실행시킬 수 있다.
        context.execute(() -> log.info("비즈니스 로직1 실행"));
        context.execute(() -> log.info("비즈니스 로직2 실행"));
    }
}
