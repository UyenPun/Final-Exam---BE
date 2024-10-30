package com.company.model.validation.department;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DepartmentNameNotExistsValidator implements ConstraintValidator<DepartmentNameNotExists, String> {

//	@Autowired
//	private IDepartmentService service;

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {

//		if (StringUtils.isEmpty(name)) {
//			return true;
//		}
//
//		return !service.isDepartmentExistsByName(name);
		return true;
	}
}