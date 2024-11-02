package com.company.model.dto.department;

import java.util.Date;

import com.company.model.entity.Account.Role;
import com.company.model.entity.Account.Status;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDTO {
	// những trường hiện ra UI
	private int id;
	private String username;
	private String fullname;
	private String email;
	private Role role;
	private Status status;
	private Date createdDateTime;
}
