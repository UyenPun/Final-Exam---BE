package com.company.config.exception;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class AuthExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

	// 401 unauthorized
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		String message = "Authentication Failed";
		String detailMessage = exception.getLocalizedMessage();
		int code = 9;
		
		ErrorResponse errorResponse = new ErrorResponse(
				message, 
				detailMessage, 
				code, 
				exception);
		
		log.error(detailMessage, exception);
		
		addErrorResponseToBodyResponse(errorResponse, response, HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	// 403 Forbidden
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException exception) throws IOException, ServletException {
		
		String message = "Access is denied";
		String detailMessage = exception.getLocalizedMessage();
		int code = 10;
		
		ErrorResponse errorResponse = new ErrorResponse(
				message, 
				detailMessage, 
				code, 
				exception);
	    
		log.error(detailMessage, exception);
		
		addErrorResponseToBodyResponse(errorResponse, response, HttpServletResponse.SC_FORBIDDEN);
	}
	
	private void addErrorResponseToBodyResponse(
			ErrorResponse errorResponse, 
			HttpServletResponse response, 
			int responsestatus)
			throws IOException {
		// convert object to json
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(errorResponse);

		// return json
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(responsestatus);
		response.getWriter().write(json);
	}
	
}