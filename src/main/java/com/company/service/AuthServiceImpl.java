package com.company.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.config.security.AccountBlockException;
import com.company.model.dto.auth.LoginInfoDTO;
import com.company.model.entity.Account;
import com.company.model.entity.Account.Status;
import com.company.model.entity.Token;
import com.company.model.form.account.CreatingAccountForm;
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

	// Register Account
	@Override
	public void createAccount(CreatingAccountForm form) {
		// create account (status = block & role = employee)
		Account entity = convertObjectToObject(form, Account.class);
		entity.setPassword(passwordEncoder.encode(form.getPassword())); // encode
		accountRepository.save(entity);

		// create token & send email
		sendAccountRegistrationTokenViaEmail(entity.getUsername());
	}

	@Override
	public void sendAccountRegistrationTokenViaEmail(String username) {
		// get account
		Account account = accountRepository.findByUsername(username);
		// create new token: đính kèm với acc đấy
		Token newRegistrationToken = tokenService.generateAccountRegistrationToken(account);
		// send email
		emailService.sendActiveAccountRegistrationEmail(account, newRegistrationToken.getKey());
	}

	// Active account
	@Override
	public void activeAccount(String registrationToken) {
		Token token = tokenService.getRegistrationTokenByKey(registrationToken);

		// active account:
		Account account = token.getAccount(); // check token của Acc nào
		account.setStatus(Status.ACTIVE);
		account.setUpdatedDateTime(new Date());
		accountRepository.save(account);

		// delete registration token
		tokenService.deleteAccountRegistrationToken(account);
	}
}
