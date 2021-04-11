package com.productheaven.catalog.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CATEGORY",indexes = {@Index (name = "IX_CATEGORY_NAME_STATUS", unique = true,columnList = "NAME, STATUS")} )
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Category {
	
	@Id
	@Column(name="ID", updatable = false, nullable = false, length = 36)
	private String id;

	@Column(name="STATUS", nullable = false)
	private int status;

	@Column(name="CREATE_TIME", nullable = false)
	private Date createTime;
	
	@Column(name="CREATED_BY", nullable = false,length = 36)
	private String createdBy;
	
	@Column(name="IMAGE_PATH", nullable = false)
	private String imagePath;
	
	@Column(name="LAST_UPDATE_TIME")
	private Date lastUpdateTime;
	
	@Column(name="LAST_UPDATED_BY",length = 36)
	private String lastUpdateBy;
	
	@Column(name="NAME", nullable = false, length = 100)
	private String name;
	
	@Column(name="DESCRIPTION", nullable = false, length = 1000)
	private String description;

	
}
