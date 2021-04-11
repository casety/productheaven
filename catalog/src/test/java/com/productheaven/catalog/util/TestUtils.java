package com.productheaven.catalog.util;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productheaven.catalog.persistence.entity.Product;

public class TestUtils {
	
	
	private TestUtils() {
		// no need for public usage
	}

	public static String objectToJsonString(final Object object) {
	    try {
	        return new ObjectMapper().writeValueAsString(object);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public <T> T stringToObject(String fromValue, Class<T> toValueType) {
	    try {
	        return new ObjectMapper().readValue(fromValue, toValueType);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	public static Product createProductEntity() {
		return createProductEntity(UUID.randomUUID().toString());
	}
	
	public static Product createProductEntity(String categoryId) {
		return Product.builder()
		.id(UUID.randomUUID().toString())
		.categoryId(categoryId)
		.description("SampleDescription")
		.imagePath("1.jpg")
		.createTime(new Date())
		.name("SampleName")
		.status(1)
		.build();
	}
}
