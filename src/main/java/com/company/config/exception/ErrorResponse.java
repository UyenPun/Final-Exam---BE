package com.company.config.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ErrorResponse {

	@NonNull
	private String message;

	@NonNull
	private String detailMessage;

	@NonNull
	private Integer code;

	private String moreInformation;

	@NonNull
	private Object exception;
}
