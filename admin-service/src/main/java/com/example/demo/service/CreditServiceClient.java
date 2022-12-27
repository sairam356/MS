package com.example.demo.service;

import java.util.Map;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.demo.dto.UserDTO;
@LoadBalancerClient(name ="credit-service")
@FeignClient(name ="credit-service/credit")
public interface CreditServiceClient {

    @PostMapping
    Map<String ,Boolean> callCreditService(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,@RequestBody UserDTO userInfo);
}
