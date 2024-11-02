package com.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.config.security.AccountBlockException;
import com.company.model.dto.auth.LoginInfoDTO;
import com.company.model.dto.auth.TokenDTO;
import com.company.model.form.account.CreatingAccountForm;
import com.company.model.form.auth.LoginForm;
import com.company.model.form.auth.ResetPasswordForm;
import com.company.model.validation.account.AccountUsernameExists;
import com.company.model.validation.account.AccountUsernameOrEmailExists;
import com.company.model.validation.auth.ForgotPasswordTokenValid;
import com.company.model.validation.auth.RefreshTokenValid;
import com.company.model.validation.auth.RegistrationTokenValid;
import com.company.service.AuthService;
import com.company.service.JWTTokenService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

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
	public LoginInfoDTO login(@RequestBody @Valid LoginForm loginForm) throws AccountBlockException {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return authService.login(loginForm.getUsername());
	}

	@GetMapping("/refreshToken")
	public TokenDTO refreshtoken(@RefreshTokenValid String refreshToken) {
		return jwtTokenService.getNewToken(refreshToken);
	}

	@PostMapping("/registration")
	public String createAccount(@Valid @RequestBody CreatingAccountForm form) {
		authService.createAccount(form);
		return "We have sent a email. Please check email to active account!";
	}

	@GetMapping("/registration/active-mail")
	public String resendAccountRegistrationTokenViaEmail(@RequestParam @AccountUsernameExists String username) {
		authService.sendAccountRegistrationTokenViaEmail(username);
		return "We have sent a email. Please check email to active account!";
	}

	@GetMapping("/registration/active")
	public String activeAccountViaEmail(@RequestParam @RegistrationTokenValid String registrationToken) {
		authService.activeAccount(registrationToken);
		return "Active success!";
	}

	// Forgot password
	@GetMapping("/password/forgot-mail")
	public String sendAccountForgotPasswordTokenViaEmail(
			@RequestParam @AccountUsernameOrEmailExists String usernameOrEmail) {
		authService.sendAccountForgotPasswordTokenViaEmail(usernameOrEmail);
		return "Email sent to your email! Please check it!";
	}

	// get Username from token
	@GetMapping("/password/forgot/username")
	public String getUsernameFromForgotPasswordToken(@ForgotPasswordTokenValid @NotBlank String forgotPasswordToken) {
		return authService.getUsernameFromForgotPasswordToken(forgotPasswordToken);
	}

	@PutMapping("/password/new-password")
	public String resetPasswordViaEmail(@Valid @RequestBody ResetPasswordForm form) {
		authService.resetPassword(form);
		return "Change password successfully!";
	}
}
