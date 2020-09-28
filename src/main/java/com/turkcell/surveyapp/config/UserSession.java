package com.turkcell.surveyapp.config;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.turkcell.surveyapp.exception.UserNotLoginException;
import com.turkcell.surveyapp.model.dto.UserDTO;

import lombok.Getter;
import lombok.Setter;

@SessionScope
@Component
@Getter
@Setter
public class UserSession {
	private UserDTO user;

	public String getUserName() {
		if (user == null) {
			throw new UserNotLoginException();
		}
		return user.getUsername();
	}

	public void clear() {
		user = null;
	}
}
