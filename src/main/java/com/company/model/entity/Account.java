package com.company.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "`Account`")
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "firstname", nullable = false, length = 50)
	private String firstname;

	@Column(name = "lastname", nullable = false, length = 50)
	private String lastname;

	@Formula(" concat(firstname,' ',lastname) ")
	private String fullname;

	@Column(name = "email", nullable = false, unique = true, length = 50)
	private String email;

	@Column(name = "username", nullable = false, unique = true, updatable = false, length = 20)
	private String username;

	@Column(name = "`password`", nullable = false, length = 800)
	private String password;

	@Column(name = "`status`", nullable = false)
	@Enumerated(EnumType.ORDINAL) // 0: Block, 1: Active
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department;

	@Column(name = "`role`", nullable = false)
	@Enumerated(EnumType.STRING) // ENUM('ADMIN', 'EMPLOYEE','MANAGER') DEFAULT 'EMPLOYEE',
	private Role role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id", referencedColumnName = "id")
	private Account creator;

	@Column(name = "created_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdDateTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifier_id", referencedColumnName = "id")
	private Account modifier;

	@Column(name = "updated_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date updatedDateTime;

	@OneToMany(mappedBy = "account")
	private List<GroupAccount> groupAccounts;

	@PrePersist
	public void setDefault() {
		if (status == null) {
			status = Status.BLOCK;
		}
		if (role == null) {
			role = Role.EMPLOYEE;
		}
	}

	public enum Status {
		BLOCK, ACTIVE;
	}

	public enum Role {
		EMPLOYEE, MANAGER, ADMIN;
	}
}