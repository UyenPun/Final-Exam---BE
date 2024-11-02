package com.company.model.form.auth;

import org.hibernate.validator.constraints.Length;

import com.company.model.validation.auth.ForgotPasswordTokenValid;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResetPasswordForm {

	@ForgotPasswordTokenValid
	@NotBlank
	private String forgotPasswordToken;

	@NotBlank
	@Length(min = 6, max = 20)
	private String newPassword;

}
