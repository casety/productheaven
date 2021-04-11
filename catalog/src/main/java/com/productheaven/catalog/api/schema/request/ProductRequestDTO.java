package com.productheaven.catalog.api.schema.request;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

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
public class ProductRequestDTO {

	@NotNull
	@Size(min=36,max=36)
	private String categoryId;
	
	private String imagePath;
	
	@NotNull
	@Size(min=1,max=100)
	private String name;
	
	@NotNull
	@Size(min=1,max=1000)
	private String description;
	
	@NotNull
	@NumberFormat(style = Style.CURRENCY)
	private Double price;

	@NotNull
	@Size(min=1,max=100)
	private String actionUser;
}
