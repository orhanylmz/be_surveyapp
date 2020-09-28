package com.turkcell.surveyapp.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.turkcell.surveyapp.config.UserSession;
import com.turkcell.surveyapp.intf.RequiredLogin;
import com.turkcell.surveyapp.model.dto.UserDTO;
import com.turkcell.surveyapp.model.request.RequestAuth;
import com.turkcell.surveyapp.service.AuthService;
import com.turkcell.surveyapp.util.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserSession userSession;
	private final AuthService authService;

	@PostMapping("/login")
	@ResponseStatus(value = HttpStatus.OK)
	public UserDTO login(@RequestBody RequestAuth request) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("login: " + Utils.objectToJsonString(request));
		authService.login(request.getUsername(), request.getPassword());
		return userSession.getUser();
	}

	@RequiredLogin
	@GetMapping("/logout")
	@ResponseStatus(value = HttpStatus.OK)
	public void logout() {
		log.info("logout: " + userSession.getUser());
		authService.logout();
	}

	@GetMapping("/me")
	@ResponseStatus(value = HttpStatus.OK)
	public String me() {
		log.info("me: " + userSession.getUser());
		if (userSession.getUser() == null) {
			return null;
		}
		return userSession.getUserName();
	}
}
