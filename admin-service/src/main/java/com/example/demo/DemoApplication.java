package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.avro.UserInfo;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

/*
@RequiredArgsConstructor
@Component
class Producer {

	private final KafkaTemplate<String, UserInfo> template;

	@EventListener(ApplicationStartedEvent.class)
	public void generate() {

		UserInfo userInfo = new UserInfo();
		userInfo.setFirstName("sairam");
		userInfo.setLastName("gollamudi");
		userInfo.setUsername("test");
	for(int i =0;i<10;i++) {
		template.send("user-p", i+"test", userInfo);
	}
	}

}*/