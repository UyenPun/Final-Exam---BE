package com.company.service;

import com.company.model.entity.Account;
import com.company.model.entity.Token;

public interface TokenService {

	Token generateAccountRegistrationToken(Account account);

	void deleteAccountRegistrationToken(Account account);

	boolean isRegistrationTokenValid(String registrationToken);

	Token getRegistrationTokenByKey(String key);
}
