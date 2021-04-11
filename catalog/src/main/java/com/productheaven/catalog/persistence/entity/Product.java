package com.productheaven.catalog.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRODUCT",indexes = {@Index (name = "ix_product_cat_id_status",columnList = "categoryId, status")} )
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Product {
	
	public Product (String id) {
		this.id = id;
	}
	
	@Id
	@Column(name="ID", updatable = false, nullable = false, length = 36)
	private String id;

	@Column(name="STATUS", nullable = false)
	private int status;

	@Column(name="CREATE_TIME", nullable = false)
	private Date createTime;
	
	@Column(name="CREATED_BY", nullable = false,length = 36)
	private String createdBy;
	
	@Column(name="LAST_UPDATE_TIME")
	private Date lastUpdateTime;
	
	@Column(name="LAST_UPDATED_BY",length = 36)
	private String lastUpdatedBy;
	
	@Column(name="CATEGORY_ID", nullable = false, length = 36)
	private String categoryId;
	
	@Column(name="NAME", nullable = false, length = 100)
	private String name;
	
	@Column(name="DESCRIPTION", nullable = false, length = 1000)
	private String description;
	
	@Column(name="IMAGE_PATH", nullable = false)
	private String imagePath;
	
	@Column(name="PRICE", nullable = false,precision =19, scale = 2)
	private double price;
	
}
