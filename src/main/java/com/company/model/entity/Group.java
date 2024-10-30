package com.company.model.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "`Group`")
public class Group implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "`name`", nullable = false, length = 100, unique = true)
	private String name;

	@Column(name = "member_size", nullable = false)
	private Integer memberSize;

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

	@OneToMany(mappedBy = "group")
	private List<GroupAccount> groupAccounts;

}
