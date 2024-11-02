package com.company.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.config.security.AccountBlockException;
import com.company.config.security.SecurityUtils;
import com.company.model.dto.auth.LoginInfoDTO;
import com.company.model.entity.Account;
import com.company.model.entity.Account.Status;
import com.company.model.entity.Token;
import com.company.model.form.account.CreatingAccountForm;
import com.company.model.form.auth.ChangePasswordForm;
import com.company.model.form.auth.ResetPasswordForm;
import com.company.repository.IAccountRepository;

@Service
@Transactional
public class AuthServiceImpl extends BaseService implements AuthService {

	@Autowired
	private JWTTokenService jwtTokenService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private IAccountRepository accountRepository;

	@Autowired
	private SecurityUtils securityUtils;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public LoginInfoDTO login(String username) throws AccountBlockException {
		// get entity
		Account entity = accountService.getAccountByUsername(username);

		if (entity.getStatus() == Status.BLOCK) {
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

	@Override
	public void createAccount(CreatingAccountForm form) {
		// create account (status = block & role = employee)
		Account entity = convertObjectToObject(form, Account.class);
		entity.setPassword(passwordEncoder.encode(form.getPassword()));
		accountRepository.save(entity);

		// create token & send email
		sendAccountRegistrationTokenViaEmail(entity.getUsername());
	}

	@Override
	public void sendAccountRegistrationTokenViaEmail(String username) {
		// get account
		Account account = accountRepository.findByUsername(username);
		// create new token
		Token newRegistrationToken = tokenService.generateAccountRegistrationToken(account);
		// send email
		emailService.sendActiveAccountRegistrationEmail(account, newRegistrationToken.getKey());
	}

	@Override
	public void activeAccount(String registrationToken) {
		Token token = tokenService.getRegistrationTokenByKey(registrationToken);

		// active account
		Account account = token.getAccount();
		account.setStatus(Status.ACTIVE);
		account.setUpdatedDateTime(new Date());
		accountRepository.save(account);

		// delete registration token
		tokenService.deleteAccountRegistrationToken(account);
	}

	@Override
	public void sendAccountForgotPasswordTokenViaEmail(String usernameOrEmail) {
		// get account
		Account account = accountRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
		// create new token
		Token newForgotPasswordToken = tokenService.generateForgotPasswordToken(account);
		// send email
		emailService.sendForgotPasswordEmail(account, newForgotPasswordToken.getKey());
	}

	@Override
	public String getUsernameFromForgotPasswordToken(String forgotPasswordToken) {
		Token token = tokenService.getForgotPasswordTokenByKey(forgotPasswordToken);
		return token.getAccount().getUsername();
	}

	@Override
	public void resetPassword(ResetPasswordForm form) {
		Token token = tokenService.getForgotPasswordTokenByKey(form.getForgotPasswordToken());

		// reset password
		Account account = token.getAccount();
		account.setPassword(passwordEncoder.encode(form.getNewPassword()));
		account.setUpdatedDateTime(new Date());
		account.setLastChangePasswordDateTime(new Date());
		accountRepository.save(account);

		// delete forgot password token
		tokenService.deleteForgotPasswordToken(account);

		// delete old refresh token
		jwtTokenService.deleteRefreshToken(account);
	}

//	Change Password
	@Override
	public void changePassword(ChangePasswordForm form) {
		// change password -> login roi
		Account account = securityUtils.getCurrentAccountLogin();
		account.setPassword(passwordEncoder.encode(form.getNewPassword()));
		account.setUpdatedDateTime(new Date());
		account.setLastChangePasswordDateTime(new Date());
		accountRepository.save(account);

		// delete old refresh token
		jwtTokenService.deleteRefreshToken(account);

		// send email
		emailService.sendChangePasswordEmail(account);
	}
}
