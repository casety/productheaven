package com.productheaven.user.api.schema.response;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UsersResponseDTO extends BaseResponseDTO {
	
	private List<UserResponseDTO> users;	
}
