package study.datajpa.advanced.trace.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import study.datajpa.advanced.trace.template.code.AbstractTemplate;
import study.datajpa.advanced.trace.template.code.SubClassLogic1;
import study.datajpa.advanced.trace.template.code.SubClassLogic2;

@Slf4j
public class TemplateMethodTest {

    @Test
    void templateMethodV0(){
        logic1();
        logic2();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        log.info("비즈니즈 로직1");
        long endTime = System.currentTimeMillis();
        long result = endTime - startTime;
        log.info("resulttime : "+result);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        log.info("비즈니즈 로직1");
        long endTime = System.currentTimeMillis();
        long result = endTime - startTime;
        log.info("resulttime : "+result);
    }

    /**
     * 템플릿 메서드 패턴 적용
     */

    @Test
    void templateMethodV1(){
        AbstractTemplate template1 = new SubClassLogic1();
        template1.execute();

        AbstractTemplate template2 = new SubClassLogic2();
        template2.execute();
    }
}
