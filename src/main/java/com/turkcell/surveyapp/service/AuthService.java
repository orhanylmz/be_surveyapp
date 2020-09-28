package com.turkcell.surveyapp.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.turkcell.surveyapp.config.UserSession;
import com.turkcell.surveyapp.entity.UserEntity;
import com.turkcell.surveyapp.exception.InvalidCredentialException;
import com.turkcell.surveyapp.util.SurveyMapper;
import com.turkcell.surveyapp.util.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserSession userSession;
	private final UserEntityService userEntityService;
	private final SurveyMapper mapper;

	public void login(String username, String password) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		String encodedPassword = Utils.encodeWithMD5(password);
		UserEntity userEntity = userEntityService.findByUsernameAndPassword(username, encodedPassword).orElseThrow(InvalidCredentialException::new);
		userSession.setUser(mapper.mapEntityToDto(userEntity));
	}

	public void logout() {
		userSession.clear();
	}
}
