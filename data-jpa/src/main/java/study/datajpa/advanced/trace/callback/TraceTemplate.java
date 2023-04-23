package study.datajpa.advanced.trace.callback;

import study.datajpa.advanced.trace.LogTrace;
import study.datajpa.advanced.trace.TraceStatus;

public class TraceTemplate {

    private final LogTrace trace;

    public TraceTemplate(LogTrace trace){
        this.trace = trace;
    }

    public <T> T execute(String message, TraceCallback<T> callback){
        TraceStatus status = null;
        try{
            status = trace.begin(message);

            //로직 호출
            T result = callback.call();

            trace.end(status);
            return result;
        }catch (Exception e){
            trace.exception(status,e);
            throw(e);
        }
    }

}

