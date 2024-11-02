package com.company.service;

import org.springframework.security.core.Authentication;

import com.company.model.dto.auth.TokenDTO;
import com.company.model.entity.Account;
import com.company.model.entity.Token;

import jakarta.servlet.http.HttpServletRequest;

public interface JWTTokenService {

	String generateJWTToken(String username);

	Authentication parseTokenToUserInformation(HttpServletRequest request);

	Token generateRefreshToken(Account account);

	boolean isRefreshTokenValid(String refreshToken);

	TokenDTO getNewToken(String refreshToken);
	
	void deleteRefreshToken(Account account);
}