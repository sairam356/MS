package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.dto.JsonholderDTO;
import com.example.demo.dto.UserDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Mono;

@Component
public class ExternalServiceCalls {

	private static final Logger logger = LoggerFactory.getLogger(ExternalServiceCalls.class);

	@Autowired
	@Lazy
	private RestTemplate restTemplate;

	@Autowired
	CreditServiceClient creditServiceClient;

	@Autowired
	WebClient webClient;

	
	@Value("${microservice.debit-service.endpoints.endpoint.uri}")
	public String debitURL;
	

	public CompletableFuture<Map<String, Boolean>> callCreditService(UserDTO userInfo) throws InterruptedException {
		CompletableFuture<Map<String, Boolean>> future = CompletableFuture
				.supplyAsync(new Supplier<Map<String, Boolean>>() {
					@Override
					public Map<String, Boolean> get() {
						logger.info(" Thread " + Thread.currentThread().getName());

						Map<String, Boolean> result = creditServiceClient.callCreditService("Bearer "+userInfo.getAccess_token(),userInfo);
						return result;
					}
				}).handle((res, ex) -> {
					if (ex != null) {
						System.out.println("Oops! We have an exception - " + ex.getMessage());
						Map<String, Boolean> map = new HashMap<String, Boolean>();

						map.put("status", false);
						return map;
					}
					return res;
				});

		return future;
	}

	@CircuitBreaker(name = "adminService", fallbackMethod = "verifyDebitServiceFallback")
	public CompletableFuture<Map<String, Boolean>> callDebitService(UserDTO userInfo) throws InterruptedException {
		CompletableFuture<Map<String, Boolean>> future = null;
		try {
			future = CompletableFuture.supplyAsync(new Supplier<Map<String, Boolean>>() {

				@Override
				public Map<String, Boolean> get() {
					logger.info("Thread " + Thread.currentThread().getName());
					HttpHeaders headers = new HttpHeaders();
					headers.add("Authorization", "Bearer " + userInfo.getAccess_token());
					 HttpEntity request = new HttpEntity(userInfo,headers);
					return restTemplate.postForObject(debitURL, request, Map.class);
				}
			});
		} catch (Exception e) {
			System.out.println("Oops! We have an exception - " + e.getMessage());
		}
		return future;
	}

	public CompletableFuture<JsonholderDTO> callJsonPlaceholder() throws InterruptedException {
		CompletableFuture<JsonholderDTO> future = CompletableFuture.supplyAsync(new Supplier<JsonholderDTO>() {
			@Override
			public JsonholderDTO get() {
				Mono<JsonholderDTO> bodyToMono = webClient.get().uri("/todos/1").retrieve()
						.bodyToMono(JsonholderDTO.class);
				return bodyToMono.block();
			}
		});
		return future;
	}

	@CircuitBreaker(name = "adminService", fallbackMethod = "verifyDebitFallback")
	public Map<String, Boolean> restTemplateCallToDebitService(UserDTO userInfo) {

		return restTemplate.postForObject(debitURL, userInfo, Map.class);

	}

	public CompletableFuture<Map<String, Boolean>> verifyDebitServiceFallback(UserDTO userInfo, Exception e) {
		logger.info(" ############# CompletableFuture fallback method is called Exception "+e.getMessage()+"###################");
		Map<String, Boolean> map = new HashMap<String, Boolean>();

		map.put("status", false);
		return CompletableFuture.completedFuture(map);

	}

	public Map<String, Boolean> verifyDebitFallback(UserDTO userInfo, Exception e) {
		logger.info("fallback method is called");
		Map<String, Boolean> map = new HashMap<String, Boolean>();

		map.put("status", true);
		return map;
	}

}
