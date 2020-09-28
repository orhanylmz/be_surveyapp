package com.turkcell.surveyapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

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
import com.turkcell.surveyapp.enumeration.Status;
import com.turkcell.surveyapp.enumeration.UserType;
import com.turkcell.surveyapp.model.dto.CategoryDTO;
import com.turkcell.surveyapp.model.dto.QuestionDTO;
import com.turkcell.surveyapp.model.dto.SurveyDTO;
import com.turkcell.surveyapp.model.dto.UserDTO;
import com.turkcell.surveyapp.model.request.RequestAuth;
import com.turkcell.surveyapp.model.request.RequestQuestion;
import com.turkcell.surveyapp.model.request.RequestUser;
import com.turkcell.surveyapp.service.CategoryService;
import com.turkcell.surveyapp.service.QuestionService;
import com.turkcell.surveyapp.service.SurveyService;
import com.turkcell.surveyapp.util.Utils;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
public class QuestionControllerTest {
	@Autowired
	private SurveyService surveyService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private QuestionService questionService;

	private String URL = "http://localhost:8080/v1/question";
	private String AUTH_URL = "http://localhost:8080/v1/auth";
	private String USER_URL = "http://localhost:8080/v1/user";

	private TestRestTemplate restTemplate = new TestRestTemplate();

	private QuestionDTO question01;
	private SurveyDTO survey;

	@PostConstruct
	public void before() throws JsonProcessingException {
		CategoryDTO category01 = categoryService.save(new CategoryDTO().setName("Category01"));

		survey = surveyService.save(new SurveyDTO().setCategory(category01).setName("Survey1").setDescription("Survey1Description"));

		question01 = questionService.save(new QuestionDTO().setQuestion("Question1").setSequence(0).setSurvey(survey));
	}

	@Test
	public void test_save_not_authorize() throws RestClientException, JsonProcessingException {
		QuestionDTO question = new QuestionDTO().setQuestion("Question3").setSequence(2).setSurvey(survey);
		RequestQuestion request = new RequestQuestion().setQuestion(question);

		ResponseEntity<QuestionDTO> response = restTemplate.exchange(URL, HttpMethod.PUT, Utils.mapToHttpEntity(request), QuestionDTO.class);

		assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void test_save() throws RestClientException, JsonProcessingException {
		RequestUser requestRegister = new RequestUser().setUsername("user").setPassword("12345").setUserType(UserType.USER);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);
		
		RequestAuth requestAuth = new RequestAuth().setUsername("user").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

		QuestionDTO question = new QuestionDTO().setQuestion("Question3").setSequence(2).setSurvey(survey);
		RequestQuestion request = new RequestQuestion().setQuestion(question);

		ResponseEntity<QuestionDTO> response = restTemplate.exchange(URL, HttpMethod.PUT, Utils.mapToHttpEntityWithCookie(request, responseLogin.getHeaders()), QuestionDTO.class);

		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		assertEquals(response.getBody().getQuestion(), "Question3");
		assertEquals(response.getBody().getStatus(), Status.PASSIVE);
	}

	@Test
	public void test_findAllBySurveyId_not_login() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		ResponseEntity<Object> response = restTemplate.getForEntity(URL + "/surveyId/" + survey.getId() + "/0", Object.class);

		assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void test_findAllBySurveyId() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		RequestUser requestRegister = new RequestUser().setUsername("admin").setPassword("12345").setUserType(UserType.ADMIN);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);
		
		RequestAuth requestAuth = new RequestAuth().setUsername("admin").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

		ResponseEntity<Object> response = restTemplate.exchange(URL + "/surveyId/" + survey.getId() + "/0", HttpMethod.GET, Utils.mapToVoidHttpEntityWithCookie(responseLogin.getHeaders()), Object.class);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void test_findById() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		RequestUser requestRegister = new RequestUser().setUsername("user").setPassword("12345").setUserType(UserType.USER);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);
		
		RequestAuth requestAuth = new RequestAuth().setUsername("user").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

		ResponseEntity<QuestionDTO> response = restTemplate.exchange(URL + "/id/" + question01.getId(), HttpMethod.GET, Utils.mapToVoidHttpEntityWithCookie(responseLogin.getHeaders()), QuestionDTO.class);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getQuestion(), question01.getQuestion());
	}

	@Test
	public void test_activate() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		RequestUser requestRegister = new RequestUser().setUsername("admin").setPassword("12345").setUserType(UserType.ADMIN);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);
		
		RequestAuth requestAuth = new RequestAuth().setUsername("admin").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

		ResponseEntity<QuestionDTO> response = restTemplate.exchange(URL + "/id/" + question01.getId() + "/activate", HttpMethod.GET, Utils.mapToVoidHttpEntityWithCookie(responseLogin.getHeaders()), QuestionDTO.class);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody().getStatus(), Status.ACTIVE);
	}
}
