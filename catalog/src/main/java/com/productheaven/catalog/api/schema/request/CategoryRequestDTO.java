package com.productheaven.catalog.api.schema.request;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	
	@NotNull
	@Size(min=1,max=100)
	private String name;
	
	@NotNull
	@Size(min=1,max=500)
	private String description;
	
	@NotNull
	@Size(min=1,max=100)
	private String actionUser;
	
}
