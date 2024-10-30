package com.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.config.security.AccountBlockException;
import com.company.model.dto.auth.LoginInfoDTO;
import com.company.model.dto.auth.TokenDTO;
import com.company.model.form.auth.LoginForm;
import com.company.service.AuthService;
import com.company.service.JWTTokenService;
import com.company.model.validation.auth.RefreshTokenValid;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "api/v1/auth")
@Validated
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@Autowired
	private JWTTokenService jwtTokenService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/login")
	public LoginInfoDTO login(@RequestBody @Valid LoginForm loginForm) throws AccountBlockException{

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginForm.getUsername(), 
						loginForm.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return authService.login(loginForm.getUsername());
	}
	
	@GetMapping("/refreshToken")
	public TokenDTO refreshtoken(@RefreshTokenValid String refreshToken) {
		return jwtTokenService.getNewToken(refreshToken);
	}
}
