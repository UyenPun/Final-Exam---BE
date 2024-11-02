package com.company.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.company.model.entity.Account;
import com.company.model.entity.Token;
import com.company.model.entity.Token.Type;
import com.company.repository.ITokenRepository;

@Service
public class TokenServiceImpl extends BaseService implements TokenService {

	@Value("${auth.registration.token.time.expiration}")
	private long AUTH_REGISTRATION_TOKEN_TIME_EXPIRATION;
	
	@Value("${auth.password.forgot.token.time.expiration}")
	private long AUTH_PASSWORD_FORGOT_TOKEN_TIME_EXPIRATION;
	
	@Autowired
	private ITokenRepository tokenRepository;

	@Override
	public Token generateAccountRegistrationToken(Account account) {
		Token registrationToken = new Token(
				account, 
				UUID.randomUUID().toString(), 
				Token.Type.REGISTER,
				new Date(new Date().getTime() + AUTH_REGISTRATION_TOKEN_TIME_EXPIRATION));
		
		// delete old registration token of this account
		deleteAccountRegistrationToken(account);

		// create new token
		return tokenRepository.save(registrationToken);
	}

	@Override
	public void deleteAccountRegistrationToken(Account account) {
		tokenRepository.deleteByTypeAndAccount(Type.REGISTER, account);
	}

	@Override
	public boolean isRegistrationTokenValid(String registrationToken) {
		Token entity = tokenRepository.findBykeyAndType(registrationToken, Type.REGISTER);
		if (entity == null || entity.getExpiredDateTime().before(new Date())) {
			return false;
		}
		return true;
	}
	
	@Override
	public Token getRegistrationTokenByKey(String key) {
		return tokenRepository.findBykeyAndType(key, Type.REGISTER);
	}

	@Override
	public boolean isForgotPasswordTokenValid(String forgotPasswordToken) {
		Token entity = tokenRepository.findBykeyAndType(forgotPasswordToken, Type.FORGOT_PASSWORD);
		if (entity == null || entity.getExpiredDateTime().before(new Date())) {
			return false;
		}
		return true;
	}

	@Override
	public Token generateForgotPasswordToken(Account account) {
		Token forgotPasswordToken = new Token(
				account, 
				UUID.randomUUID().toString(), 
				Token.Type.FORGOT_PASSWORD,
				new Date(new Date().getTime() + AUTH_PASSWORD_FORGOT_TOKEN_TIME_EXPIRATION));
		
		// delete old forgot password token of this account
		deleteForgotPasswordToken(account);

		// create new token
		return tokenRepository.save(forgotPasswordToken);
	}
	
	@Override
	public void deleteForgotPasswordToken(Account account) {
		tokenRepository.deleteByTypeAndAccount(Type.FORGOT_PASSWORD, account);
	}

	@Override
	public Token getForgotPasswordTokenByKey(String key) {
		return tokenRepository.findBykeyAndType(key, Type.FORGOT_PASSWORD);
	}
}
