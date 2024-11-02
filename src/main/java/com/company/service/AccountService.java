package com.company.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.company.model.entity.Account;

public interface AccountService extends UserDetailsService {

	Account getAccountByUsername(String username);

	boolean isAccountExistsByUsername(String username);

	boolean isAccountExistsByEmail(String email);

	boolean isOldPasswordCorrect(String oldPassword);
}
