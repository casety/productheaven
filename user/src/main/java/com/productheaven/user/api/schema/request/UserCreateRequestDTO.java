package com.productheaven.user.api.schema.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.productheaven.user.util.MessageKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequestDTO {
	
	@NotNull(message = "{"+MessageKey.VALIDATION_USERNAME_EMPTY+"}")
	@Size(max = 50,message = "{"+MessageKey.VALIDATION_USERNAME_INVALID+"}")
	private String username;
	
	@NotNull(message = "{"+MessageKey.VALIDATION_EMAIL_EMPTY+"}")
	@Email(message = "{"+MessageKey.VALIDATION_EMAIL_INVALID+"}")
	@Size(min=1,max = 100,message = "{"+MessageKey.VALIDATION_EMAIL_INVALID+"}")
	private String email;
	
	@NotNull(message = "{"+MessageKey.VALIDATION_NAME_EMPTY+"}")
	@Size(min=1,max = 100,message = "{"+MessageKey.VALIDATION_NAME_INVALID+"}")
	private String name;
	
	@NotNull(message = "{"+MessageKey.VALIDATION_SURNAME_EMPTY+"}")
	@Size(min=1,max = 100,message = "{"+MessageKey.VALIDATION_SURNAME_INVALID+"}")
	private String surname;
	
	@NotNull(message = "{"+MessageKey.VALIDATION_PASSWORD_EMPTY+"}")
	@Size(min=8,max = 20,message = "{"+MessageKey.VALIDATION_PASSWORD_INVALID+"}")
	private String password;
	
}
