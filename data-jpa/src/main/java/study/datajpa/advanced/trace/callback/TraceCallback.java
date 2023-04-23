package study.datajpa.advanced.trace.callback;

public interface TraceCallback<T> {
    T call();
}
