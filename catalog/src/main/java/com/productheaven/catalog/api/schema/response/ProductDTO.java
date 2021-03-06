package com.productheaven.catalog.api.schema.response;

import java.util.Date;

import lombok.Data;

@Data
public class ProductDTO {

	private String id;

	private Date createTime;
	
	private String createdBy;
	
	private Date lastUpdatedTime;
	
	private String lastUpdatedBy;
		
	private String categoryId;
	
	private String name;
	
	private String description;
	
	private String imagePath;
	
	private Double price;
	
}
