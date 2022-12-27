package com.credit.creditservice;

import java.util.HashMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.credit.creditservice.dto.UserDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/credit")

public class CreditController {



	private static final String URL = "http://localhost:9494/debit";

	@PostMapping
	public Map<String, Boolean> receiveNewUserInfo(@RequestBody UserDTO userDTO) {

		System.out.println(" Received to  CreditService : " + userDTO.toString());
		Map<String, Boolean> map = new HashMap<String, Boolean>();

		map.put("status", true);

		return map;

	}

}
