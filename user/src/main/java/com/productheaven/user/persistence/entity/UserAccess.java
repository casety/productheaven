package com.productheaven.user.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Table(name = "USER_ACCESS")
@Data
public class UserAccess {

	@Id
	@GeneratedValue(generator = "UserAccessIdGenerator")
	@GenericGenerator(name = "UserAccessIdGenerator", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name="ID", updatable = false, nullable = false, length = 36)
	private String id;
	
	@Column(name="CREATE_TIME", nullable = false)
	private Date createTime;
	
	@Column(name = "USER_ID", nullable = false, length = 36)
	private String userId;
	
	@Column(name = "HTTP_RESOURCE", nullable = false, length = 50)
	private String httpResource;
	
	@Column(name = "HTTP_METHOD", nullable = false, length = 10)
	private String httpMethod;
}
