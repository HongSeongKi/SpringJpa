package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import study.datajpa.proxy.v1.AppV1Config;
//import study.datajpa.proxy.v1.proxy.InterfaceProxyCofig;
import study.datajpa.proxy.v1.proxy.InterfaceProxyCofig;
import study.datajpa.proxy.v2.Appv2Config;
import study.datajpa.proxy.v2.proxy.ConcreteProxyConfig;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
//@Import({AppV1Config.class,Appv2Config.class})
//@Import({Appv2Config.class})
@Import(ConcreteProxyConfig.class)
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}


	@Bean
	public AuditorAware<String> auditorProvider(){
		return new AuditorAware<String>() {
			@Override
			public Optional<String> getCurrentAuditor() {
				return Optional.of(UUID.randomUUID().toString());
			}
		};
	}
}
