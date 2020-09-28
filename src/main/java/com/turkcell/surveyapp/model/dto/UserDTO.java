package com.turkcell.surveyapp.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.turkcell.surveyapp.enumeration.UserType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class UserDTO extends BaseDTO {
	private String username;
	@JsonIgnore
	private String password;
	private UserType userType;
}
