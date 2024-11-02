package com.company.model.validation.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.company.service.AccountService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OldPasswordCorrectValidator implements ConstraintValidator<OldPasswordCorrect, String> {

	@Autowired
	private AccountService accountService;

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValid(String oldPassword, ConstraintValidatorContext context) {

		if (StringUtils.isEmpty(oldPassword)) {
			return false;
		}

		return accountService.isOldPasswordCorrect(oldPassword);
	}
}
