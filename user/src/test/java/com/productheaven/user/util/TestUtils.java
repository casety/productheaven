package com.productheaven.user.util;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productheaven.user.persistence.entity.User;

public class TestUtils {
	
	private TestUtils() {
		// TODO Auto-generated constructor stub
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
	
	public static User createUserEntity() {
		String tempId = UUID.randomUUID().toString();
		User user = new User(tempId,new Date(),"sampleusername","samplePass","samplePass","sample@email","samplename","samplesurname");
		return user;
	}
	
}
