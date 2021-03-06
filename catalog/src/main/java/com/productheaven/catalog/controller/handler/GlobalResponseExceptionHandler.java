package com.productheaven.catalog.controller.handler;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.productheaven.catalog.api.schema.response.BaseResponseDTO;
import com.productheaven.catalog.service.exception.CategoryAlreadyExistsException;
import com.productheaven.catalog.service.exception.CategoryNotFoundException;
import com.productheaven.catalog.service.exception.InvalidRequestException;
import com.productheaven.catalog.service.exception.ProductNotFoundException;
import com.productheaven.catalog.util.MessageKey;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalResponseExceptionHandler extends ResponseEntityExceptionHandler {

	private MessageSource messageSource;
	
	public GlobalResponseExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	// handle argument level validations
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,  final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		StringBuilder builder = new StringBuilder(produceMessageFromMessageSource(MessageKey.VALIDATION_COMMON_MESSAGE));
        for (final ObjectError error : ex.getBindingResult().getAllErrors()) {        
        	builder.append(error.getDefaultMessage() +" ");
        }
		BaseResponseDTO baseResponseDTO = new BaseResponseDTO(builder.toString());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(value = CategoryNotFoundException.class)
	protected ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException ex, WebRequest request) {
		BaseResponseDTO baseResponseDTO = new BaseResponseDTO(produceMessageFromException(ex));
		return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(value = ProductNotFoundException.class)
	protected ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {
		BaseResponseDTO baseResponseDTO = new BaseResponseDTO(produceMessageFromException(ex));
		return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = CategoryAlreadyExistsException.class)
	protected ResponseEntity<Object> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException ex, WebRequest request) {
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
		log.error(e.getMessage(), e);
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
