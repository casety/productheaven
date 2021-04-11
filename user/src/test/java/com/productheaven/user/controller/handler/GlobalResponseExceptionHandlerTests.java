package com.productheaven.user.controller.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.productheaven.user.config.MessageSourceConfig;
import com.productheaven.user.service.exception.InvalidRequestException;
import com.productheaven.user.service.exception.NoUsersFoundException;
import com.productheaven.user.service.exception.UserAlreadyExistsException;
import com.productheaven.user.service.exception.UserNotFoundException;
import com.productheaven.user.util.MessageKey;

@ExtendWith(SpringExtension.class)
@Import(MessageSourceConfig.class)
class GlobalResponseExceptionHandlerTests {

	
	GlobalResponseExceptionHandler handler;
	
	@Autowired
	MessageSource messageSource;
	
	@BeforeEach
	void init () {
		handler = new GlobalResponseExceptionHandler(messageSource);
	}
	
	@Test
	void whenNoUsersFoundExceptionOccurs_httpStatus404ShouldBeReturned () {
		ResponseEntity<Object> result = handler.handleNoUsersFoundException(new NoUsersFoundException(),null);
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertTrue(result.getBody().toString().contains(produceMessageFromMessageSource(MessageKey.EXCEPTION_NoUsersFoundException)));
	}
	
	@Test
	void whenUsersNotFoundExceptionOccurs_httpStatus404ShouldBeReturned () {
		ResponseEntity<Object> result = handler.handleUserNotFoundException(new UserNotFoundException(),null);
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertTrue(result.getBody().toString().contains(produceMessageFromMessageSource(MessageKey.EXCEPTION_UserNotFoundException)));
	}
	
	@Test
	void whenUserAlreadyExistsExceptionOccurs_httpStatus404ShouldBeReturned () {
		ResponseEntity<Object> result = handler.handleUserAlreadyExistsException(new UserAlreadyExistsException(),null);
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertTrue(result.getBody().toString().contains(produceMessageFromMessageSource(MessageKey.EXCEPTION_UserAlreadyExistsException)));
	}
	
	@Test
	void whenInvalidRequestExceptionOccurs_httpStatus400ShouldBeReturned () {
		String message = "sampleMessage";
		ResponseEntity<Object> result = handler.handleInvalidRequestException(new InvalidRequestException(message));
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertTrue(result.getBody().toString().contains(message));
	}
	
	private String produceMessageFromMessageSource(String messageKey) {
		return messageSource.getMessage(messageKey, null, new Locale("tr", "TR"));
	}
	
	
}
