package study.datajpa.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTemplate { //template 만들기

    public void execute(){
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        call();
        long endTime = System.currentTimeMillis();
        long result = endTime - startTime;
        log.info("resulttime : "+result);
    }

    protected abstract void call();
}
