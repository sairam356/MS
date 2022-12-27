package com.example.demo.config;

import java.util.concurrent.Executor;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableAsync
public class DemoKakfaConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoKakfaConfig.class);

	@Bean
	public NewTopic topic1() {
		return TopicBuilder.name("user-p").partitions(10).replicas(3).compact().build();
	}

	@Bean(name = "taskExecutor")

	public Executor taskExecutor() {
		LOGGER.debug("Creating Async Task Executor");
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(25);
		executor.setThreadNamePrefix("AdminThread-");
		executor.initialize();
		return executor;
	}

	@LoadBalanced
	@Bean
	public WebClient getWebClient() {
		return WebClient.builder().baseUrl("https://jsonplaceholder.typicode.com/")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}
	
	@LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
