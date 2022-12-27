package com.debit.debitservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DebitServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebitServiceApplication.class, args);
	}

}
