package com.productheaven.user.api.schema.request;

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
public class UserLoginRequestDTO {
	
	@NotNull(message = "{"+MessageKey.VALIDATION_USERNAME_EMPTY+"}")
	@Size(max = 50,message = "{"+MessageKey.VALIDATION_USERNAME_INVALID+"}")
	private String username;
	
	@NotNull(message = "{"+MessageKey.VALIDATION_PASSWORD_EMPTY+"}")
	@Size(min=1,max = 50,message = "{"+MessageKey.VALIDATION_PASSWORD_INVALID+"}")
	private String password;
	
}
