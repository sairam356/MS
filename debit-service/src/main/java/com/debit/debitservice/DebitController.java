package com.debit.debitservice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debit")
public class DebitController {
	
	
	@PostMapping
	public Map<String ,Boolean>  receiveNewUserInfo(@RequestBody UserDTO userDTO) {
		
		System.out.println(" Received to  DebitService : "+userDTO.toString());
		Map<String, Boolean> map = new HashMap<String, Boolean>();
        
		map.put("status", true);

		return map;
	}
	@Secured("ROLE_developer")
	@GetMapping("/{name}")
	public String getUserName(@PathVariable String name, @AuthenticationPrincipal Jwt jwt) {
		
		Map<String, Object> claims = jwt.getClaims();
		return (String) claims.get("family_name");
	}
	
	//hasAuthority('ROLE_developer') or 
	@PreAuthorize("#id == #jwt.subject") 
	
    @DeleteMapping(path = "/{id}")
    public String deleteUser(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return "Deleted user with id " + id + " and JWT subject " + jwt.getSubject();
    }
	

	@PostAuthorize("returnObject.id == #jwt.subject")
    @GetMapping(path = "/id/{id}")
    public UserDTO getUserId(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
		  
		System.out.println(jwt.getSubject());
		
		System.out.println(id);
		
        return new UserDTO((String)jwt.getSubject(),(String)jwt.getClaims().get("family_name"),null,null);
    }

}
