package com.company.config.Internationalization;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MessageProperty {

	@Autowired
	private MessageSource messageSource;

	public String getMessage(String key) {
		return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
	}

	public String getViMessage(String key) {
		return messageSource.getMessage(key, null, new Locale("vi", "VN"));
	}

	public String getEnMessage(String key) {
		return messageSource.getMessage(key, null, Locale.US);
	}
}