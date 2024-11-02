package com.company.model.dto.account;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImportedAccountInfoDTO {
	private int id;
	private String username;
	private String fullname;
}
