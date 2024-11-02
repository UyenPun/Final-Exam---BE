package com.company.model.dto.account;

import com.company.model.entity.Account.Role;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountNoDepartmentDTO {
	// trả ra
	private int id;
	private String username;
	private String fullname;
	private String email;
	private Role role;
}
