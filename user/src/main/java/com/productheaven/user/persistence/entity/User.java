package com.productheaven.user.persistence.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {
	
	public User(String id) {
		this.id = id;
	}
	
	@Id
	@Column(name="ID", updatable = false, nullable = false, length = 36)
	private String id;

	@Column(name="CREATE_TIME", nullable = false)
	private Date createTime;
	
	@Column(name="USERNAME", nullable = false, length = 100)
	private String username;
	
	@Column(name="PASSWORD_HASHED", nullable = false, length = 512)
	private String passwordHashed;
	
	@Column(name="EMAIL", nullable = false, length = 50)
	private String email;
	
	@Column(name="NAME", nullable = false, length = 100)
	private String name;
	
	@Column(name="SURNAME", nullable = false, length = 100)
	private String surname;
	
	
}
