package com.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.service.AccountService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(value = "/api/v1/accounts")
@Validated
@Log4j2
public class AccountController {

	@Autowired
	private AccountService accountService;

	@GetMapping("/username/exists")
	public boolean isAccountExistsByUsername(String username) {
		return accountService.isAccountExistsByUsername(username);
	}

	@GetMapping("/email/exists")
	public boolean isAccountExistsByEmail(String email) {
		return accountService.isAccountExistsByEmail(email);
	}

	@GetMapping("/usernameOrEmail/exists")
	public boolean isAccountExistsByUsernameOrEmail(String usernameOrEmail) {
		return accountService.isAccountExistsByUsername(usernameOrEmail) || accountService.isAccountExistsByEmail(usernameOrEmail);
	}
}
