package com.company.model.validation.account;

import org.springframework.beans.factory.annotation.Autowired;

import com.company.service.AccountService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccountIdExistsValidator implements ConstraintValidator<AccountIdExists, Integer> {

	@Autowired
	private AccountService service;

	@Override
	public boolean isValid(Integer id, ConstraintValidatorContext context) {

		if (id == null || id <= 0) {
			return false;
		}

		return service.isAccountExistsById(id);
	}
}