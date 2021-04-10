package com.productheaven.user.service;

import com.productheaven.user.service.exception.InvalidRequestException;

public interface RequestValidationService {
	
	public void validateUserId(String userId) throws InvalidRequestException;

}
