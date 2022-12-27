package com.example.demo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class JsonholderDTO {

	private String userId;
	
	private String id;
	
	private String title;
	
	private Boolean completed;
}
