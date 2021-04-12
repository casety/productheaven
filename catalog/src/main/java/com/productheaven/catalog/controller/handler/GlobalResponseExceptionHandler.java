package com.productheaven.catalog.controller.handler;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.productheaven.catalog.api.schema.response.BaseResponseDTO;
import com.productheaven.catalog.service.exception.InvalidRequestException;
import com.productheaven.catalog.service.exception.ProductNotFoundException;

@ControllerAdvice
//@Slf4j
public class GlobalResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
	
	
	
	@ExceptionHandler(value = ProductNotFoundException.class)
	protected ResponseEntity<Object> handleUserNotFoundException(ProductNotFoundException ex, WebRequest request) {
		BaseResponseDTO baseResponseDTO = new BaseResponseDTO(produceMessageFromException(ex));
		return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
	}
	
	//raised when request validation occurs
	@ExceptionHandler(value = InvalidRequestException.class)
	protected ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException e) {
		BaseResponseDTO baseResponseDTO = new BaseResponseDTO(produceMessageFromMessageSource(e.getMessage()));
		return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
	}
	
	// handle all other cases properly
	@ExceptionHandler(value = Exception.class)
	protected ResponseEntity<Object> handleAllException(Exception e) {
		//log.error(e.getMessage(), e);
		BaseResponseDTO baseResponseDTO = new BaseResponseDTO(produceMessageFromException(e));
		return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private String produceMessageFromMessageSource(String messageKey) {
		return messageSource.getMessage(messageKey, null, new Locale("tr", "TR"));
	}
	
	private String produceMessageFromException(Exception exception) {
		String messageKey = String.format("exception.%s.message", exception.getClass().getSimpleName());
		return messageSource.getMessage(messageKey, null, new Locale("tr", "TR"));
	}
}
