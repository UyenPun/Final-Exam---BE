package com.company.model.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "`Group_Account`")
public class GroupAccount implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private GroupAccountKey id;

	@ManyToOne
	@MapsId("group_id")
	@JoinColumn(name = "group_id")
	private Group group;

	@ManyToOne
	@MapsId("account_id")
	@JoinColumn(name = "account_id")
	private Account account;

	@Column(name = "join_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date joinDateTime;

	@Data
	@NoArgsConstructor
	@Embeddable
	public static class GroupAccountKey implements Serializable {

		private static final long serialVersionUID = 1L;

		@Column(name = "group_id", nullable = false)
		private Integer groupId;

		@Column(name = "account_id", nullable = false)
		private Integer accountId;
	}
}