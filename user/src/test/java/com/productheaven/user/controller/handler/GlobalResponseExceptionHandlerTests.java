package com.productheaven.user.controller.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.productheaven.user.service.exception.InvalidRequestException;
import com.productheaven.user.service.exception.NoUsersFoundException;
import com.productheaven.user.service.exception.UserNotFoundException;

@ExtendWith(SpringExtension.class)
class GlobalResponseExceptionHandlerTests {

	@InjectMocks
	GlobalResponseExceptionHandler handler;
	
	@Test
	void whenNoUsersFoundExceptionOccurs_httpStatus404ShouldBeReturned () {
		ResponseEntity<Object> result = handler.handleNoUsersFoundException(new NoUsersFoundException(),null);
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertTrue(result.getBody().toString().contains("Sistemde tanimli kullanici bulunamadi"));
	}
	
	@Test
	void whenUsersNotFoundExceptionOccurs_httpStatus404ShouldBeReturned () {
		ResponseEntity<Object> result = handler.handleUserNotFoundException(new UserNotFoundException(),null);
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertTrue(result.getBody().toString().contains("Sorgulanan kullanici bulunamadi"));
	}
	
	@Test
	void whenInvalidRequestExceptionOccurs_httpStatus400ShouldBeReturned () {
		String message = "sampleMessage";
		ResponseEntity<Object> result = handler.handleInvalidRequestException(new InvalidRequestException(message));
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertTrue(result.getBody().toString().contains(message));
	}
	
}
