package hu.farago.data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@ComponentScan(basePackages = {"hu.farago.data"})
@EnableAsync
@EnableScheduling
public class AppConfig {
	
	public static final String APPLICATION = "application";
	public static final String APPLICATION_TEST = "application-test";

	@Profile(APPLICATION)
	@Configuration
	@PropertySource("classpath:application.properties")
	@PropertySource("file:///C:/DEV/servers/external_configs/data-downloader-application.properties")
	public static class ApplicationProperties {
	}

	@Profile(APPLICATION_TEST)
	@Configuration
	@PropertySource("classpath:application-test.properties")
	public static class ApplicationTestProperties {
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean(name = "taskScheduler")
	public ThreadPoolTaskScheduler taskScheduler() {
	    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
	    scheduler.setPoolSize(5);
	    scheduler.setWaitForTasksToCompleteOnShutdown(true);
	    scheduler.setRemoveOnCancelPolicy(true);

	    return scheduler;
	}
	
}