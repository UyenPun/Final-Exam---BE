package com.company.model.validation.account;

import org.springframework.beans.factory.annotation.Autowired;

import com.company.service.AccountService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccountNoDepartmentValidator implements ConstraintValidator<AccountNoDepartment, Integer> {

	@Autowired
	private AccountService accountService;

	@Override
	public boolean isValid(Integer accountId, ConstraintValidatorContext context) {

		if (accountId == null || accountId <= 0) {
			return true;
		}

		return accountService.getAccountById(accountId).getDepartment() == null;
	}
}