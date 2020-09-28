package com.turkcell.surveyapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.turkcell.surveyapp.enumeration.UserType;
import com.turkcell.surveyapp.model.dto.CategoryDTO;
import com.turkcell.surveyapp.model.dto.OptionDTO;
import com.turkcell.surveyapp.model.dto.QuestionDTO;
import com.turkcell.surveyapp.model.dto.SurveyDTO;
import com.turkcell.surveyapp.model.dto.UserDTO;
import com.turkcell.surveyapp.model.request.RequestAuth;
import com.turkcell.surveyapp.model.request.RequestOption;
import com.turkcell.surveyapp.model.request.RequestUser;
import com.turkcell.surveyapp.service.CategoryService;
import com.turkcell.surveyapp.service.OptionService;
import com.turkcell.surveyapp.service.QuestionService;
import com.turkcell.surveyapp.service.SurveyService;
import com.turkcell.surveyapp.util.Utils;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
public class OptionControllerTest {
	@Autowired
	private SurveyService surveyService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private OptionService optionService;

	private String URL = "http://localhost:8080/v1/option";
	private String AUTH_URL = "http://localhost:8080/v1/auth";
	private String USER_URL = "http://localhost:8080/v1/user";

	private TestRestTemplate restTemplate = new TestRestTemplate();

	List<OptionDTO> options;

	private QuestionDTO question01;
	private QuestionDTO question02;

	private SurveyDTO survey;

	@PostConstruct
	public void before() throws JsonProcessingException {
		CategoryDTO category01 = categoryService.save(new CategoryDTO().setName("Category01"));

		survey = surveyService.save(new SurveyDTO().setCategory(category01).setName("Survey1").setDescription("Survey1Description"));

		question01 = questionService.save(new QuestionDTO().setQuestion("Question1").setSequence(0).setSurvey(survey));
		question02 = questionService.save(new QuestionDTO().setQuestion("Question2").setSequence(1).setSurvey(survey));

		options = new ArrayList<OptionDTO>();
		options.add(new OptionDTO().setOption("Option1").setQuestion(question02).setSequence(0));
		options.add(new OptionDTO().setOption("Option2").setQuestion(question02).setSequence(1));
		options.add(new OptionDTO().setOption("Option3").setQuestion(question02).setSequence(2));
		options.add(new OptionDTO().setOption("Option4").setQuestion(question02).setSequence(3));
		options = optionService.saveAll(options);
	}

	@Test
	public void test_save_not_authorize() throws RestClientException, JsonProcessingException {
		OptionDTO option = new OptionDTO().setOption("Option01").setQuestion(question01).setSequence(0);
		RequestOption request = new RequestOption().setOption(option);

		ResponseEntity<Object> response = restTemplate.exchange(URL, HttpMethod.PUT, Utils.mapToHttpEntity(request), Object.class);

		assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void test_save() throws RestClientException, JsonProcessingException {
		RequestUser requestRegister = new RequestUser().setUsername("user").setPassword("12345").setUserType(UserType.USER);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);

		RequestAuth requestAuth = new RequestAuth().setUsername("user").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

		OptionDTO option = new OptionDTO().setOption("Option01").setQuestion(question01).setSequence(0);
		RequestOption request = new RequestOption().setOption(option);

		ResponseEntity<Object> response = restTemplate.exchange(URL, HttpMethod.PUT, Utils.mapToHttpEntityWithCookie(request, responseLogin.getHeaders()), Object.class);

		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
	}

	@Test
	public void test_findAllByQuestionId_not_login() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		ResponseEntity<Object> response = restTemplate.getForEntity(URL + "/questionId/" + question01.getId(), Object.class);

		assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void test_findAllByQuestionId() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		RequestUser requestRegister = new RequestUser().setUsername("user").setPassword("12345").setUserType(UserType.USER);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);

		RequestAuth requestAuth = new RequestAuth().setUsername("user").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

		ResponseEntity<Object> response = restTemplate.exchange(URL + "/questionId/" + question01.getId(), HttpMethod.GET, Utils.mapToVoidHttpEntityWithCookie(responseLogin.getHeaders()), Object.class);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void test_select_not_login() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		ResponseEntity<Object> response = restTemplate.getForEntity(URL + "/id/1/select", Object.class);

		assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void test_select() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		RequestUser requestRegister = new RequestUser().setUsername("user").setPassword("12345").setUserType(UserType.USER);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);

		RequestAuth requestAuth = new RequestAuth().setUsername("user").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

		ResponseEntity<Void> response = restTemplate.exchange(URL + "/id/" + options.get(0).getId() + "/select", HttpMethod.GET, Utils.mapToVoidHttpEntityWithCookie(responseLogin.getHeaders()), Void.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void test_activate() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		RequestUser requestRegister = new RequestUser().setUsername("admin").setPassword("12345").setUserType(UserType.ADMIN);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);

		RequestAuth requestAuth = new RequestAuth().setUsername("admin").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

//		OptionDTO option = new OptionDTO().setOption("Option01").setQuestion(question01).setSequence(0);
//		RequestOption request = new RequestOption().setOption(option);
//		restTemplate.exchange(URL, HttpMethod.PUT, Utils.mapToHttpEntity(request), Object.class);

		ResponseEntity<Object> response = restTemplate.exchange(URL + "/id/" + options.get(0).getId() + "/activate", HttpMethod.GET, Utils.mapToVoidHttpEntityWithCookie(responseLogin.getHeaders()), Object.class);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
}
