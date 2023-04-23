package study.datajpa.advanced.trace;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {

    @Bean //스프링 컨테이너가 자동으로 읽어서 빈으로 등록한다.
    public LogTrace logTrace(){
        return new ThreadLocalLogTrace(); //싱글톤으로 등록된다.
    }
}
