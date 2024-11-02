package com.company.model.form.auth;

import org.hibernate.validator.constraints.Length;

import com.company.model.validation.auth.OldPasswordCorrect;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePasswordForm {
	// new pass and old pass
	@OldPasswordCorrect
	@NotBlank
	private String oldPassword;

	@NotBlank
	@Length(min = 6, max = 20)
	private String newPassword;

}
