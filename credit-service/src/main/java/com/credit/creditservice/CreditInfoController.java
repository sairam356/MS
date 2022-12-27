package com.credit.creditservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/creditInfo")
public class CreditInfoController {

	
	@GetMapping
	public String  getCreditInfo() {
		
		return "Success";
	}
}
