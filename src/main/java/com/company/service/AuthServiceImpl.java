package com.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.config.security.AccountBlockException;
import com.company.model.dto.auth.LoginInfoDTO;
import com.company.model.entity.Account;
import com.company.model.entity.Account.Status;
import com.company.model.entity.Token;

@Service
public class AuthServiceImpl extends BaseService implements AuthService {

	@Autowired
	private JWTTokenService jwtTokenService;

	@Autowired
	private AccountService accountService;

	@Override
	public LoginInfoDTO login(String username) throws AccountBlockException {
		// get entity
		Account entity = accountService.getAccountByUsername(username);

		// check status (0,1 in dtb)
		if (entity.getStatus() == Status.BLOCK) { // 1
			throw new AccountBlockException("Your account is blocked!");
		}

		// convert entity to dto
		LoginInfoDTO dto = convertObjectToObject(entity, LoginInfoDTO.class);

		// add jwt token to dto
		dto.setToken(jwtTokenService.generateJWTToken(entity.getUsername()));

		// add refresh token to dto
		Token token = jwtTokenService.generateRefreshToken(entity);
		dto.setRefreshToken(token.getKey());

		return dto;
	}
}
