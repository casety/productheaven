package com.productheaven.user.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.productheaven.user.api.schema.request.UserLoginRequestDTO;
import com.productheaven.user.api.schema.response.BaseResponseDTO;
import com.productheaven.user.service.LoginService;
import com.productheaven.user.service.exception.LoginAttemptFailedException;

/**
 * 
 * HTTP LoginController for the user operations
 * 
 * @author mkaman
 * 
 * */

@RestController

public class LoginController {
	
	private LoginService loginService;
	
	@Autowired
	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<BaseResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO loginRequest) throws LoginAttemptFailedException  {
		loginService.loginWith(loginRequest.getUsername(), loginRequest.getPassword());
		return new ResponseEntity<>(new BaseResponseDTO(),HttpStatus.OK);
	}	
}
