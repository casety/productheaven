package com.productheaven.catalog.util;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productheaven.catalog.api.schema.request.ProductRequestDTO;
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
		.createdBy("Tester")
		.createTime(new Date())
		.name("SampleName")
		.status(1)
		.price(12.34)
		.build();
	}
	
	public static ProductRequestDTO createProductRequestDTO() {
		return createProductRequestDTO(UUID.randomUUID().toString());
	}
	
	public static ProductRequestDTO createProductRequestDTO(String categoryId) {
		return ProductRequestDTO.builder()
		.categoryId(categoryId)
		.actionUser("Tester")
		.description("SampleDescription")
		.imagePath("1.jpg")
		.name("SampleName")
		.price(11.11)
		.build();
	}
	
	public static ProductRequestDTO createProductUpdateRequestDTO() {
		return createProductUpdateRequestDTO(UUID.randomUUID().toString());
	}
	
	public static ProductRequestDTO createProductUpdateRequestDTO(String categoryId) {
		return ProductRequestDTO.builder()
		.categoryId(categoryId)
		.actionUser("Tester-Updated")
		.description("SampleDescription-Updated")
		.imagePath("1-Updated.jpg")
		.name("SampleName-Updated")
		.price(22.22)
		.build();
	}
}
