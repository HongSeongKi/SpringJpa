package study.datajpa.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@Slf4j
public class ThreadLocalService {
    //쓰레드 로컬을 사용하면 본인의 저장소에 저장이되어서 동시성 문제 해결가능
    private ThreadLocal<String> nameStore = new ThreadLocal<>();

    public String logic(String name){
        log.info("저장 name = {} -> nameStore ={}",name,nameStore.get());
        nameStore.set(name);
        sleep(1000);
        log.info("조회 nameStore = {}",nameStore.get());
        return nameStore.get();
    }

    private void sleep(int mills){
        try{
            Thread.sleep(mills);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
