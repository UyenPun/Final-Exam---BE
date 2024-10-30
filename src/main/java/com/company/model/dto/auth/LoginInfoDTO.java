package com.company.model.dto.auth;

import com.company.model.entity.Account.Status;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginInfoDTO {

	private Integer id;

	private String fullname;

	private String email;

	private Status status;

	private String departmentName;

	private String role;

	private String token;

	private String refreshToken;
}
