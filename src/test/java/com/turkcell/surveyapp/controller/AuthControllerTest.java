package com.turkcell.surveyapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.turkcell.surveyapp.enumeration.UserType;
import com.turkcell.surveyapp.model.dto.UserDTO;
import com.turkcell.surveyapp.model.request.RequestAuth;
import com.turkcell.surveyapp.model.request.RequestUser;
import com.turkcell.surveyapp.util.Utils;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
public class AuthControllerTest {
	private String URL = "http://localhost:8080/v1/auth";
	private String USER_URL = "http://localhost:8080/v1/user";
	
	private TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Test
	public void test_login() throws RestClientException, JsonProcessingException {
		RequestUser requestRegister = new RequestUser().setUsername("user").setPassword("12345").setUserType(UserType.USER);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);
		
		RequestAuth request = new RequestAuth().setUsername("user").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(URL + "/login", request, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);
		assertEquals(responseLogin.getBody().getUsername(), "user");
		assertEquals(responseLogin.getBody().getUserType(), UserType.USER);
		
		HttpEntity<String> httpEntity = Utils.mapToVoidHttpEntityWithCookie(responseLogin.getHeaders());
		ResponseEntity<String> responseCheckLogin = restTemplate.exchange(URL + "/me", HttpMethod.GET, httpEntity, String.class);
		
		assertEquals(responseCheckLogin.getStatusCode(), HttpStatus.OK);
		assertEquals(responseCheckLogin.getBody(), "user");
	}
	
	@Test
	public void test_logout() throws RestClientException, JsonProcessingException {
		RequestUser requestRegister = new RequestUser().setUsername("user").setPassword("12345").setUserType(UserType.USER);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);
		
		RequestAuth request = new RequestAuth().setUsername("user").setPassword("12345");
		ResponseEntity<Void> responseLogin = restTemplate.postForEntity(URL + "/login", request, Void.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);
		
		HttpEntity<String> httpEntity = Utils.mapToVoidHttpEntityWithCookie(responseLogin.getHeaders());
		ResponseEntity<String> responseCheckLogin = restTemplate.exchange(URL + "/me", HttpMethod.GET, httpEntity, String.class);
		
		assertEquals(responseCheckLogin.getStatusCode(), HttpStatus.OK);
		assertEquals(responseCheckLogin.getBody(), "user");
		
		ResponseEntity<Void> responseLogout = restTemplate.exchange(URL + "/logout", HttpMethod.GET, httpEntity, Void.class);
		assertEquals(responseLogout.getStatusCode(), HttpStatus.OK);
		
		responseCheckLogin = restTemplate.exchange(URL + "/me", HttpMethod.GET, httpEntity, String.class);
		
		assertEquals(responseCheckLogin.getStatusCode(), HttpStatus.OK);
		assertNull(responseCheckLogin.getBody());
	}

}
