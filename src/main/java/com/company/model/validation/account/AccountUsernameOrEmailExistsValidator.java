package com.company.model.validation.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.company.service.AccountService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccountUsernameOrEmailExistsValidator implements ConstraintValidator<AccountUsernameOrEmailExists, String> {

	@Autowired
	private AccountService service;

	@SuppressWarnings("deprecation")
	@Override
	public boolean isValid(String usernameOrEmail, ConstraintValidatorContext context) {

		if (StringUtils.isEmpty(usernameOrEmail)) {
			return true;
		}

		return service.isAccountExistsByUsername(usernameOrEmail) || service.isAccountExistsByEmail(usernameOrEmail);
	}
}