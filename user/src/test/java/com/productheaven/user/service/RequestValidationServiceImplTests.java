package com.productheaven.user.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.productheaven.user.service.exception.InvalidRequestException;
import com.productheaven.user.service.impl.RequestValidationServiceImpl;

@ExtendWith(SpringExtension.class)
class RequestValidationServiceImplTests {
	
	@InjectMocks
	RequestValidationServiceImpl service;

	@Test
	void whenValidUuidisGiven_validateUserIdShoulExecuteSuccessfully () throws InvalidRequestException{
		service.validateUserId(UUID.randomUUID().toString());
	}
	
	@Test
	void whenInvalidUuidisGiven_validateUserIdShouldThrowInvalidRequestException () throws InvalidRequestException{
	    assertThrows(InvalidRequestException.class, () -> {
	    	service.validateUserId(UUID.randomUUID().toString()+".");
	    });
	    assertThrows(InvalidRequestException.class, () -> {
	    	service.validateUserId("."+UUID.randomUUID().toString());
	    });
	    assertThrows(InvalidRequestException.class, () -> {
	    	service.validateUserId("-");
	    });
	}
}
