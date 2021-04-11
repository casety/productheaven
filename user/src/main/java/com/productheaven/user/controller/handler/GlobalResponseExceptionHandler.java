package com.productheaven.user.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.productheaven.user.api.schema.response.BaseResponseDTO;
import com.productheaven.user.service.exception.InvalidRequestException;
import com.productheaven.user.service.exception.NoUsersFoundException;
import com.productheaven.user.service.exception.UserAlreadyExistsException;
import com.productheaven.user.service.exception.UserNotFoundException;

@ControllerAdvice
public class GlobalResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = NoUsersFoundException.class)
	protected ResponseEntity<Object> handleNoUsersFoundException(NoUsersFoundException ex, WebRequest request) {
		BaseResponseDTO baseResponseDTO = new BaseResponseDTO("Sistemde tanimli kullanici bulunamadi");
		return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = UserNotFoundException.class)
	protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
		BaseResponseDTO baseResponseDTO = new BaseResponseDTO("Sorgulanan kullanici bulunamadi");
		return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = UserAlreadyExistsException.class)
	protected ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
		BaseResponseDTO baseResponseDTO = new BaseResponseDTO("Kullanici daha onceden kayit olmus durumda");
		return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = InvalidRequestException.class)
	protected ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException e) {
		BaseResponseDTO baseResponseDTO = new BaseResponseDTO(e.getMessage());
		return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
	}
}
