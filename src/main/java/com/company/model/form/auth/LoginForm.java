package com.company.model.form.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginForm {

	@NotBlank
	private String username;

	@NotBlank
	private String password;

}
