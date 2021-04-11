package com.productheaven.catalog.api.schema.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO extends BaseResponseDTO {
	
	private ProductDTO product;

}
