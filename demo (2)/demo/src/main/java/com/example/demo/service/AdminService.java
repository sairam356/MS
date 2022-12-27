package com.example.demo.service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.JsonholderDTO;
import com.example.demo.dto.UserDTO;

@Service
public class AdminService {
	private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

	@Autowired
	ExternalServiceCalls  exServiceCalls;

	
	public void sendInfoToAllServices(UserDTO userDTO) {
		try {
			long start = System.currentTimeMillis();
			CompletableFuture<Map<String, Boolean>> creditServiceResult = sendUserInfoTOCreditService(userDTO);
			CompletableFuture<Map<String, Boolean>> debitServiceResult = sendUserInfoTODebitService(userDTO);
			CompletableFuture<JsonholderDTO> jsonHolderResult = getJsonHolderInfo();
			CompletableFuture.allOf(debitServiceResult, creditServiceResult, jsonHolderResult).thenAccept(r -> {
				try {
					Optional<Map<String, Boolean>>  dResult = Optional.ofNullable(debitServiceResult.get());				
					Optional<Map<String, Boolean>> cRestlt = Optional.ofNullable(creditServiceResult.get());
					Optional<JsonholderDTO> jsonholderDTO = Optional.ofNullable(jsonHolderResult.get());
					if (dResult.isPresent() && cRestlt.isPresent()) {
						System.out.println(
								"################### Both Credit and Debit Services are verified Sucessfully ###################");
						System.out.println("################### CreditService Status ###################"+ cRestlt.get().get("status")+"###################");
						   System.out.println("################### DebitService Status ###################" + dResult.get().get("status"));
					}
					long end = System.currentTimeMillis();
					System.out.println("########## StartTime and EndTime  " + (end - start) + "ms ###################");
					System.out.println("############ JsonholderDTO  " + jsonholderDTO.get());

				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}).join();

			CompletableFuture<Void> cpFuture = logRsunAsyn();
			cpFuture.get();
		} catch (InterruptedException | ExecutionException e) {
             e.printStackTrace();
		}
	}
	
	public CompletableFuture<Map<String, Boolean>> sendUserInfoTOCreditService(UserDTO userInfo) {

		try {
			return exServiceCalls.callCreditService(userInfo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;

	}

	public CompletableFuture<Map<String, Boolean>> sendUserInfoTODebitService(UserDTO userInfo) {
		try {
			return exServiceCalls.callDebitService(userInfo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;

	}

	public CompletableFuture<JsonholderDTO> getJsonHolderInfo() {
		try {
			return exServiceCalls.callJsonPlaceholder();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public CompletableFuture<Void> logRsunAsyn() {
		CompletableFuture<Void> runSynFurture = CompletableFuture
				.runAsync(() -> System.out.println(" ############Logging in RunAsync mode #########"));

		return runSynFurture;

	}

}
