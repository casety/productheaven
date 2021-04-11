package com.productheaven.user.api.schema.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequestDTO {
	
	@NotNull
	private String username;
	
	@NotNull
	@Email
	private String email;
	
	@NotNull
	@Min(1)
	@Max(50)
	private String name;
	
	@NotNull
	@Min(1)
	@Max(50)
	private String surname;
	
	@NotNull
	@Min(1)
	@Max(512)
	private String passwordHashed;//hashed

}
