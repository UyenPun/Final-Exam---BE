package com.company.service;

import com.company.config.security.AccountBlockException;
import com.company.model.dto.auth.LoginInfoDTO;
import com.company.model.form.account.CreatingAccountForm;
import com.company.model.form.auth.ResetPasswordForm;

public interface AuthService {

	LoginInfoDTO login(String username) throws AccountBlockException;

	void createAccount(CreatingAccountForm form);

	void sendAccountRegistrationTokenViaEmail(String username);

	void activeAccount(String registrationToken);

	void sendAccountForgotPasswordTokenViaEmail(String usernameOrEmail);

	String getUsernameFromForgotPasswordToken(String forgotPasswordToken);
	
	void resetPassword(ResetPasswordForm form);
}
