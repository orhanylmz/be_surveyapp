package com.turkcell.surveyapp.model.request;

import com.turkcell.surveyapp.enumeration.UserType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class RequestUser {
	private String username;
	private String password;
	private UserType userType;
}
