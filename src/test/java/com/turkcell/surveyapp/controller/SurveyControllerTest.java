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
import com.turkcell.surveyapp.enumeration.UserType;
import com.turkcell.surveyapp.model.dto.CategoryDTO;
import com.turkcell.surveyapp.model.dto.SurveyDTO;
import com.turkcell.surveyapp.model.dto.UserDTO;
import com.turkcell.surveyapp.model.request.RequestAuth;
import com.turkcell.surveyapp.model.request.RequestSurvey;
import com.turkcell.surveyapp.model.request.RequestUser;
import com.turkcell.surveyapp.service.CategoryService;
import com.turkcell.surveyapp.service.SurveyService;
import com.turkcell.surveyapp.util.Utils;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
public class SurveyControllerTest {
	@Autowired
	private SurveyService surveyService;

	@Autowired
	private CategoryService categoryService;

	private String URL = "http://localhost:8080/v1/survey";
	private String AUTH_URL = "http://localhost:8080/v1/auth";
	private String USER_URL = "http://localhost:8080/v1/user";

	private TestRestTemplate restTemplate = new TestRestTemplate();
	
	private CategoryDTO category01;
	private CategoryDTO category02;
	
	private SurveyDTO survey;

	@PostConstruct
	public void before() throws JsonProcessingException {
		category01 = categoryService.save(new CategoryDTO().setName("Category01"));
		category02 = categoryService.save(new CategoryDTO().setName("Category02"));
		
		surveyService.save(new SurveyDTO().setCategory(category02).setName("Survey2").setDescription("Survey2Description"));
		surveyService.save(new SurveyDTO().setCategory(category02).setName("Survey3").setDescription("Survey3Description"));
		survey = surveyService.save(new SurveyDTO().setCategory(category02).setName("Survey4").setDescription("Survey4Description"));
	}

	@Test
	public void test_save_not_authorize() throws RestClientException, JsonProcessingException {
		SurveyDTO survey = new SurveyDTO().setCategory(category01).setName("Survey1").setDescription("Survey1Description");
		RequestSurvey request = new RequestSurvey().setSurvey(survey);
		
		ResponseEntity<SurveyDTO> response = restTemplate.exchange(URL, HttpMethod.PUT, Utils.mapToHttpEntity(request), SurveyDTO.class);
		
		assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}
	
	@Test
	public void test_save_for_user() throws RestClientException, JsonProcessingException {
		RequestUser requestRegister = new RequestUser().setUsername("user").setPassword("12345").setUserType(UserType.USER);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);
		
		RequestAuth requestAuth = new RequestAuth().setUsername("user").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);
		
		SurveyDTO survey = new SurveyDTO().setCategory(category01).setName("Survey1").setDescription("Survey1Description");
		RequestSurvey request = new RequestSurvey().setSurvey(survey);
		
		ResponseEntity<SurveyDTO> response = restTemplate.exchange(URL, HttpMethod.PUT, Utils.mapToHttpEntityWithCookie(request, responseLogin.getHeaders()), SurveyDTO.class);
		
		assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
	}
	
	@Test
	public void test_save_for_admin() throws RestClientException, JsonProcessingException {
		RequestUser requestRegister = new RequestUser().setUsername("admin").setPassword("12345").setUserType(UserType.ADMIN);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);
		
		RequestAuth requestAuth = new RequestAuth().setUsername("admin").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);
		
		SurveyDTO survey = new SurveyDTO().setCategory(category01).setName("Survey1").setDescription("Survey1Description");
		RequestSurvey request = new RequestSurvey().setSurvey(survey);
		
		ResponseEntity<SurveyDTO> response = restTemplate.exchange(URL, HttpMethod.PUT, Utils.mapToHttpEntityWithCookie(request, responseLogin.getHeaders()), SurveyDTO.class);
		
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		assertNotNull(response.getBody().getId());
		assertEquals(response.getBody().getName(), "Survey1");
	}
	
	@Test
	public void test_findAll_not_login() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		ResponseEntity<Object> response = restTemplate.getForEntity(URL + "/0", Object.class);
		
		assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}
	
	@Test
	public void test_findAll() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		RequestUser requestRegister = new RequestUser().setUsername("admin").setPassword("12345").setUserType(UserType.ADMIN);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);
		
		RequestAuth requestAuth = new RequestAuth().setUsername("admin").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);
		
		ResponseEntity<Object> response = restTemplate.exchange(URL + "/0", HttpMethod.GET, Utils.mapToVoidHttpEntityWithCookie(responseLogin.getHeaders()), Object.class);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void test_findAllByCategoryId() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		RequestUser requestRegister = new RequestUser().setUsername("admin").setPassword("12345").setUserType(UserType.ADMIN);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);
		
		RequestAuth requestAuth = new RequestAuth().setUsername("admin").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

		ResponseEntity<Object> response = restTemplate.exchange(URL + "/categoryId/"+category02.getId()+"/0", HttpMethod.GET, Utils.mapToVoidHttpEntityWithCookie(responseLogin.getHeaders()), Object.class);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void test_findById() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		RequestUser requestRegister = new RequestUser().setUsername("admin").setPassword("12345").setUserType(UserType.ADMIN);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);
		
		RequestAuth requestAuth = new RequestAuth().setUsername("admin").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

		ResponseEntity<SurveyDTO> response = restTemplate.exchange(URL + "/id/" + survey.getId(), HttpMethod.GET, Utils.mapToVoidHttpEntityWithCookie(responseLogin.getHeaders()), SurveyDTO.class);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getName(), survey.getName());
	}
}
