package com.productheaven.catalog.service;

import com.productheaven.catalog.service.exception.InvalidRequestException;

public interface RequestValidationService {
	
	public void validateProductId(String productId) throws InvalidRequestException;

	public void validateCategoryId(String categoryId) throws InvalidRequestException;

}
