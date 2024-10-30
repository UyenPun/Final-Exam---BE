package com.company.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.company.model.entity.Account;

public interface AccountService extends UserDetailsService {

	public Account getAccountByUsername(String username);
}
