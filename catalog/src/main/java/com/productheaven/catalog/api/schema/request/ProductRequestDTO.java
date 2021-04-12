package com.productheaven.catalog.api.schema.request;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

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
public class ProductRequestDTO {

	@NotNull(message = "{"+MessageKey.VALIDATION_CATEGORY_ID_EMPTY+"}")
	@Size(min=36,max=36,message = "{"+MessageKey.VALIDATION_CATEGORY_ID_INVALID+"}")
	private String categoryId;
	
	private String imagePath;
	
	@NotNull(message = "{"+MessageKey.VALIDATION_PRODUCT_NAME_EMPTY+"}")
	@Size(min=1,max=100,message = "{"+MessageKey.VALIDATION_PRODUCT_NAME_INVALID+"}")
	private String name;
	
	@NotNull(message = "{"+MessageKey.VALIDATION_PRODUCT_DESC_EMPTY+"}")
	@Size(min=1,max=1000,message = "{"+MessageKey.VALIDATION_PRODUCT_DESC_INVALID+"}")
	private String description;
	
	@NotNull(message = "{"+MessageKey.VALIDATION_PRODUCT_PRICE_EMPTY+"}")
	@NumberFormat(style = Style.CURRENCY)
	@Min(value = 10,message = "{"+MessageKey.VALIDATION_PRODUCT_PRICE_LOW+"}")
	@Max(value = 100000,message = "{"+MessageKey.VALIDATION_PRODUCT_PRICE_HIGH+"}")
	private Double price;

	@NotNull(message = "{"+MessageKey.VALIDATION_ACT_USER_EMPTY+"}")
	@Size(min=1,max=100,message = "{"+MessageKey.VALIDATION_ACT_USER_INVALID+"}")
	private String actionUser;

}
