package com.turkcell.surveyapp.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.turkcell.surveyapp.enumeration.UserType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "survey_user")
public class UserEntity extends BaseEntity {
	@Id
	private String username;

	private String password;

	@Enumerated(EnumType.STRING)
	private UserType userType;
}
