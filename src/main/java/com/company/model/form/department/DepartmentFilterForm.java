package com.company.model.form.department;

import java.util.Date;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DepartmentFilterForm {

	private String q; // search

	@PastOrPresent
	private Date minCreatedDate;

	private Date maxCreatedDate;

	@PositiveOrZero
	private Integer minMemberSize;

	@PositiveOrZero
	private Integer maxMemberSize;
}
