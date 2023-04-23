package study.datajpa.advanced.trace;

import org.aspectj.weaver.tools.Trace;

public interface LogTrace {

    TraceStatus begin(String message);
    void end(TraceStatus status);
    void exception(TraceStatus status, Exception e);

}
