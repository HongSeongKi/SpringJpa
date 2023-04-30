package study.datajpa.advanced.trace.template;

import lombok.extern.slf4j.Slf4j;
import study.datajpa.advanced.trace.LogTrace;
import study.datajpa.advanced.trace.TraceStatus;

@Slf4j
public abstract class AbstractTemplate<T> { //템플릿메서드 패턴 -> 부모 클래스의 메소드를 넣어두고 자식은 상속받아서 해결 , but 부모코드에서 문제가 생기면 자식에게도 영향을 받는다.

    private final LogTrace trace;

    public AbstractTemplate(LogTrace trace){
        this.trace = trace;
    }

    public T execute(String message) {
        TraceStatus status = null;
        try{
            status = trace.begin(message);

            //로직 호출
            T result = call();

            trace.end(status);
            return result;
        }catch (Exception e){
            trace.exception(status,e);
            throw(e);
        }
    }

    public abstract T call(); //변하는 이부분은 자식 클래스에서 구현


}
