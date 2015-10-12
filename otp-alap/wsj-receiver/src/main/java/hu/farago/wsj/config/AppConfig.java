package hu.farago.wsj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = {"hu.farago.wsj"})
@EnableAsync
@EnableScheduling
public class AppConfig {
	
	public static final String APPLICATION = "application";
	public static final String APPLICATION_TEST = "application-test";
	public static final String APPLICATION_TEST_SIMPLE = "application-test-simple";

	@Profile(APPLICATION)
	@Configuration
	@PropertySource("classpath:application.properties")
	public static class ApplicationProperties {
	}

	@Profile(APPLICATION_TEST)
	@Configuration
	@PropertySource("classpath:application-test.properties")
	public static class ApplicationTestProperties {
	}
	
	@Profile(APPLICATION_TEST_SIMPLE)
	@Configuration
	@PropertySource("classpath:application-test-simple.properties")
	public static class ApplicationTestSimpleProperties {
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	
}