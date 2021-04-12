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
public class CategoryRequestDTO {
	
	private String imagePath;
	
	@NotNull(message = "{"+MessageKey.VALIDATION_CATEGORY_NAME_EMPTY+"}")
	@Size(min=1,max=100,message = "{"+MessageKey.VALIDATION_CATEGORY_NAME_INVALID+"}")
	private String name;
	
	@NotNull(message = "{"+MessageKey.VALIDATION_CATEGORY_DESC_EMPTY+"}")
	@Size(min=1,max=500,message = "{"+MessageKey.VALIDATION_CATEGORY_DESC_INVALID+"}")
	private String description;
	
	@NotNull(message = "{"+MessageKey.VALIDATION_ACT_USER_EMPTY+"}")
	@Size(min=1,max=100,message = "{"+MessageKey.VALIDATION_ACT_USER_INVALID+"}")
	private String actionUser;
	
}
