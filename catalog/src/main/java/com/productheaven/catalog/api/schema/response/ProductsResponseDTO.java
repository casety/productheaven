package com.productheaven.catalog.api.schema.response;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductsResponseDTO extends BaseResponseDTO {
	
	private List<ProductDTO> products;	
}
