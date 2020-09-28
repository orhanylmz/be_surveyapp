package com.turkcell.surveyapp.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.turkcell.surveyapp.config.Constants;
import com.turkcell.surveyapp.intf.RequiredAdmin;
import com.turkcell.surveyapp.model.dto.UserDTO;
import com.turkcell.surveyapp.model.request.RequestUser;
import com.turkcell.surveyapp.model.response.ResponsePage;
import com.turkcell.surveyapp.service.UserService;
import com.turkcell.surveyapp.util.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PutMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public UserDTO save(@RequestBody RequestUser request) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("save: " + Utils.objectToJsonString(request));
		return userService.save(
				new UserDTO()
				.setUsername(request.getUsername())
				.setPassword(request.getPassword())
				.setUserType(request.getUserType()));
	}

	@RequiredAdmin
	@GetMapping("/{page}")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponsePage<UserDTO> findAll(@PathVariable Integer page) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("findAll: page: " + page);
		return new ResponsePage<UserDTO>()
				.setList(userService.findAll(PageRequest.of(page, Constants.PAGE_SIZE)));
	}
}
