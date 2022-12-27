package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserDTO;
import com.example.demo.service.AdminService;
import com.example.demo.service.ExternalServiceCalls;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService adminService;

	@PostMapping
	public Map<String, Boolean> verifyAdminervice(@RequestBody UserDTO userDTO, @AuthenticationPrincipal Jwt jwt) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();

		System.out.println("In verifyDebitService ");
		userDTO.setAccess_token(jwt.getTokenValue());
		try {
			 adminService.sendInfoToAllServices(userDTO);
			 
		} catch (Exception e) {
			map.put("status", false);
			e.printStackTrace();
		}
		return map;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public CompletableFuture<Map<String, String>> callTest(UserDTO userInfo) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		CompletableFuture<Map<String, String>> future = CompletableFuture
				.supplyAsync(new Supplier<Map<String, String>>() {
					@Override
					public Map<String, String> get() {

						if (userInfo.getLastName().equals("sairam")) {

							result.put("status", "success");
							return result;
						} else if (userInfo.getLastName().equals("sai")) {

							throw new NullPointerException("Value cannot be null");
						} else {

							return null;
						}

					}
				}).exceptionally(exception -> {
					System.err.println("exception: " + exception);
					result.put("status", "failure");
					return result;
				});
		return future;
	}

	public void test(UserDTO userDTO) {
		try {
			callTest(userDTO).thenApply(x -> {

				return "UserCreation" + x.get("stauts");

			}).thenApply(y -> {

				return y + "happy";
			}).thenApplyAsync(x -> {

				return x + " :)";

			}).thenAccept(t -> {
				System.out.println(" To do Something ");

			});

			System.out.println("test");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
