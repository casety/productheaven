package com.productheaven.user.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.productheaven.user.persistence.entity.User;
import com.productheaven.user.service.LoginService;
import com.productheaven.user.service.UserService;
import com.productheaven.user.service.exception.LoginAttemptFailedException;
import com.productheaven.user.service.exception.UserNotFoundException;

@Service
public class LoginServiceImpl implements LoginService {

	private UserService userService;
	
	private PasswordEncoder encoder;
	
	public LoginServiceImpl(UserService userService,PasswordEncoder encoder) {
		this.userService = userService;
		this.encoder = encoder;
	}
	
	@Override
	public void loginWith(String username,String password) throws LoginAttemptFailedException {
		User userByUsername;
		try {
			userByUsername = userService.getUserByUsername(username);
			boolean matches = encoder.matches(password, userByUsername.getPasswordHashed());
			if (!matches) {
				throw new LoginAttemptFailedException();
			}
		} catch (UserNotFoundException e) {
			throw new LoginAttemptFailedException();
		}
	}

}
