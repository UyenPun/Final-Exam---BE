package com.company.config.security;

public class AccountBlockException extends Exception {

	private static final long serialVersionUID = 1L;

	public AccountBlockException(String message) {
		super(message);
	}
}
