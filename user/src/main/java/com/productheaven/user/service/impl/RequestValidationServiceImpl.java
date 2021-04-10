package com.productheaven.user.service.impl;

import org.springframework.stereotype.Service;

import com.productheaven.user.service.RequestValidationService;
import com.productheaven.user.service.exception.InvalidRequestException;

@Service
public class RequestValidationServiceImpl implements RequestValidationService {

	private static final String REGEX_FOR_UUID = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$";

	@Override
	public void validateUserId(String userId) throws InvalidRequestException {

		if (!userId.matches(REGEX_FOR_UUID)) {
			throw new InvalidRequestException("Gecersiz id");
		}
	}

}
