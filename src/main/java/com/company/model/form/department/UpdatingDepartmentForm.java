package com.company.model.form.department;

import org.hibernate.validator.constraints.Length;

import com.company.model.validation.account.AccountIdExists;
import com.company.model.validation.department.DepartmentNameNotExists;
import com.company.model.validation.interf.NameNotExistsGroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatingDepartmentForm {

	@NotBlank
	@Length(max = 100)
	@DepartmentNameNotExists(groups = NameNotExistsGroup.class)
	private String name;

	@NotNull
	@AccountIdExists
	private Integer managerId;
}