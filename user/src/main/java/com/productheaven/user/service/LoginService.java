package com.productheaven.user.service;

import com.productheaven.user.service.exception.LoginAttemptFailedException;

public interface LoginService {

	void loginWith(String username, String password) throws LoginAttemptFailedException;

}