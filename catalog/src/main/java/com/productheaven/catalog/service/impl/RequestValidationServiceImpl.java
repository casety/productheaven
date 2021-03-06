package com.productheaven.catalog.service.impl;

import org.springframework.stereotype.Service;

import com.productheaven.catalog.service.RequestValidationService;
import com.productheaven.catalog.service.exception.InvalidRequestException;
import com.productheaven.catalog.util.MessageKey;

@Service
public class RequestValidationServiceImpl implements RequestValidationService {

	private static final String REGEX_FOR_UUID = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$";

	@Override
	public void validateProductId(String productId) throws InvalidRequestException {

		if (!productId.matches(REGEX_FOR_UUID)) {
			throw new InvalidRequestException(MessageKey.VALIDATION_PRODUCT_ID_INVALID);
		}
	}
	
	@Override
	public void validateCategoryId(String categoryId) throws InvalidRequestException {

		if (!categoryId.matches(REGEX_FOR_UUID)) {
			throw new InvalidRequestException(MessageKey.VALIDATION_CATEGORY_ID_INVALID);
		}
	}
}
