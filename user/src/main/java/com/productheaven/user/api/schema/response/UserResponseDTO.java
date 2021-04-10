package com.productheaven.user.api.schema.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponseDTO extends BaseResponseDTO {
	
	private UserDTO user;

}
