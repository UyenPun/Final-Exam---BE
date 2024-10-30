package com.company.service;

import com.company.config.security.AccountBlockException;
import com.company.model.dto.auth.LoginInfoDTO;

public interface AuthService {

	LoginInfoDTO login(String username) throws AccountBlockException;
}
