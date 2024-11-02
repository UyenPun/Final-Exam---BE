package com.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.company.model.entity.Account;

@Service
public class EmailServiceImpl extends BaseService implements EmailService {

	@Value("${server.protocol}")
	private String SERVER_PROTOCOL;

	@Value("${server.hostname}")
	private String SERVER_HOSTNAME;

	@Value("${server.port}")
	private long SERVER_PORT;

	@Autowired
	private JavaMailSender mailSender;

	private void sendEmail(String recipientEmail, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(recipientEmail);
		message.setSubject(subject);
		message.setText(content);

		mailSender.send(message);
	}

	// send mail - Register Account
	@Override
	public void sendActiveAccountRegistrationEmail(Account account, String registrationToken) {
		// link để active
		String activeAccountUrl = String.format("%s://%s:%d/api/v1/auth/registration/active?registrationToken=%s",
				SERVER_PROTOCOL, SERVER_HOSTNAME, SERVER_PORT, registrationToken);
		String subject = "[FinalExam] Active Account";
		String content = "You have successfully registered an account\n"
				+ "Click on the link below to activate account\n" + activeAccountUrl;

		sendEmail(account.getEmail(), subject, content);
	}

}
