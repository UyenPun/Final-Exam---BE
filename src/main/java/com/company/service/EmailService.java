package com.company.service;

import com.company.model.entity.Account;

public interface EmailService {

	void sendActiveAccountRegistrationEmail(Account account, String registrationToken);
}
