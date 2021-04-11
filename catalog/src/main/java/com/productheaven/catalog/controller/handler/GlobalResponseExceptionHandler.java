package com.productheaven.catalog.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.productheaven.catalog.api.schema.response.BaseResponseDTO;
import com.productheaven.catalog.service.exception.NoProductsFoundException;
import com.productheaven.catalog.service.exception.ProductNotFoundException;

@ControllerAdvice
public class GlobalResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = NoProductsFoundException.class)
	protected ResponseEntity<Object> handleNoUsersFoundException(NoProductsFoundException ex, WebRequest request) {
		BaseResponseDTO baseResponseDTO = new BaseResponseDTO("Sistemde tanimli urun bulunamadi");
		return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = ProductNotFoundException.class)
	protected ResponseEntity<Object> handleUserNotFoundException(ProductNotFoundException ex, WebRequest request) {
		BaseResponseDTO baseResponseDTO = new BaseResponseDTO("Sorgulanan urun bulunamadi");
		return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
	}
}
