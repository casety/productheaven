package com.productheaven.catalog.api.schema.request;


import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequestDTO {

	@NotNull
	@Max(50)
	private String categoryId;
	
	private Integer imageId;
	
	@NotNull
	@Max(100)
	private String name;
	
	@NotNull
	@Max(1000)
	private String description;
	
	@NumberFormat(style = Style.CURRENCY)
	private Double price;
	
}
