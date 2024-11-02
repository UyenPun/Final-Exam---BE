package com.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.company.config.security.SecurityUtils;
import com.company.model.entity.Account;
import com.company.repository.IAccountRepository;

@Service
public class AccountServiceImpl extends BaseService implements AccountService {

	@Autowired
	private IAccountRepository accountRepository;
	
	@Autowired
	private SecurityUtils securityUtils;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Account account = accountRepository.findByUsername(username);

		if (account == null) {
			throw new UsernameNotFoundException(username);
		}

		return new User(
				account.getUsername(), 
				account.getPassword(), 
				AuthorityUtils.createAuthorityList(account.getRole().toString()));
	}

	@Override
	public Account getAccountByUsername(String username) {
		return accountRepository.findByUsername(username);
	}

	@Override
	public boolean isAccountExistsByUsername(String username) {
		return accountRepository.existsByUsername(username);
	}

	@Override
	public boolean isAccountExistsByEmail(String email) {
		return accountRepository.existsByEmail(email);
	}

	@Override
	public boolean isOldPasswordCorrect(String oldPassword) {
		return passwordEncoder.matches(
				oldPassword, 
				securityUtils.getCurrentAccountLogin().getPassword());
	}
}
