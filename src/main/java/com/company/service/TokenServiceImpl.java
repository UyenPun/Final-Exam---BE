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

	@Value("${account.registration.token.time.expiration}")
	private long ACCOUNT_REGISTRATION_TOKEN_TIME_EXPIRATION;

	@Autowired
	private ITokenRepository tokenRepository;

	// Register Account
	@Override
	public Token generateAccountRegistrationToken(Account account) {
		Token registrationToken = new Token(account, UUID.randomUUID().toString(), Token.Type.REGISTER,
				new Date(new Date().getTime() + ACCOUNT_REGISTRATION_TOKEN_TIME_EXPIRATION));

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
}
