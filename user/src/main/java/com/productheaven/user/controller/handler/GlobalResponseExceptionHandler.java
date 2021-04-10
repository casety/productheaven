package com.productheaven.user.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.productheaven.user.api.schema.response.BaseResponseDTO;
import com.productheaven.user.service.exception.NoUsersFoundException;

@ControllerAdvice
public class GlobalResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = NoUsersFoundException.class)
	protected ResponseEntity<Object> handleNoUsersFoundException(NoUsersFoundException ex, WebRequest request) {
		BaseResponseDTO baseResponseDTO = new BaseResponseDTO("Kullanici bulunamadi");
		return new ResponseEntity<>(baseResponseDTO,HttpStatus.NOT_FOUND);
	}
	
}
