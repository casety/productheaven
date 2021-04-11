package com.productheaven.user.api.schema.response;

import lombok.Data;

@Data
public class UserDTO {
	
	private String id;
	
	private String username;
	
	private String email;
	
	private String name;
	
	private String surname;

	//to be handled by gateway
	private String passwordHashed;

	

}
