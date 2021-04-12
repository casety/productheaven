package com.productheaven.catalog.api.schema.request;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.productheaven.catalog.util.MessageKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteCategoryRequestDTO {
	
	@NotNull(message = "{"+MessageKey.VALIDATION_ACT_USER_EMPTY+"}")
	@Size(min=1,max=100,message = "{"+MessageKey.VALIDATION_ACT_USER_INVALID+"}")
	private String actionUser;
}
