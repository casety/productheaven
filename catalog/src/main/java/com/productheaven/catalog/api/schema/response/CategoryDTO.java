package com.productheaven.catalog.api.schema.response;

import java.util.Date;

import lombok.Data;

@Data
public class CategoryDTO {

	private String id;

	private Date createTime;
	
	private String createdBy;
	
	private String lastUpdatedBy;
		
	private String name;
	
	private String description;
	
	private String imagePath;
	
}
