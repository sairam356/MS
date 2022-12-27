package com.example.demo;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.demo.dto.UserDTO;
import com.example.demo.service.AdminService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserInfoConsumer {

	@Autowired
	AdminService adminService;

	@KafkaListener(topics = { "user-p" }, groupId = "spring-boot-kafka")
	public void consume(ConsumerRecord<String, GenericRecord> record) {
		System.out.println("received = " + record.value() + " with key " + record.key());
		UserDTO userDTO = null;
		String userInfo = record.value().toString();
		String access_token = record.key();

		ObjectMapper mapper = new ObjectMapper();
		try {
			userDTO = mapper.readValue(userInfo, UserDTO.class);
			userDTO.setAccess_token(access_token);
		
			adminService.sendInfoToAllServices(userDTO);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
