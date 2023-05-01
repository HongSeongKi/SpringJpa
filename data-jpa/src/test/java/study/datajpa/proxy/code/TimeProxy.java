package study.datajpa.proxy.code;

import lombok.extern.slf4j.Slf4j;
import study.datajpa.proxy.v1.proxy.ConcreateLogic;

@Slf4j
public class TimeProxy extends ConcreateLogic {

    private ConcreateLogic logic;

    public TimeProxy(ConcreateLogic logic){
        this.logic = logic;
    }

    @Override
    public String operation() {
        log.info("TimeDecorator 실행");
        long startTime = System.currentTimeMillis();
        String result = logic.operation();
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("Timedecorator 종료 resultTime = {}ms",resultTime);
        return result;
    }
}
