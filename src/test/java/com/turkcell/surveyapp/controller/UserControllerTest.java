package com.turkcell.surveyapp.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

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
public class UserControllerTest {
	private String URL = "http://localhost:8080/v1/user";
	private String AUTH_URL = "http://localhost:8080/v1/auth";

	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void test_save() throws RestClientException, JsonProcessingException {
		RequestUser request = new RequestUser().setUsername("user").setPassword("12345").setUserType(UserType.USER);
		ResponseEntity<UserDTO> response = restTemplate.exchange(URL, HttpMethod.PUT, Utils.mapToHttpEntity(request), UserDTO.class);

		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getUsername());
		assertNull(response.getBody().getPassword());
		assertThat(response.getBody().getUsername()).isEqualTo("user");
		assertThat(response.getBody().getUserType()).isEqualTo(UserType.USER);
	}

	@Test
	public void test_findAll_for_not_login() throws RestClientException, JsonProcessingException {
		RequestUser request = new RequestUser().setUsername("user").setPassword("12345").setUserType(UserType.USER);
		restTemplate.exchange(URL, HttpMethod.PUT, Utils.mapToHttpEntity(request), UserDTO.class);
		
		ResponseEntity<Object> response = restTemplate.getForEntity(URL + "/0", Object.class);

		assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void test_findAll_for_user_login() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		RequestUser requestRegister = new RequestUser().setUsername("user").setPassword("12345").setUserType(UserType.USER);
		restTemplate.exchange(URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);
		
		RequestAuth request = new RequestAuth().setUsername("user").setPassword("12345");
		ResponseEntity<Void> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", request, Void.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

		HttpEntity<String> httpEntity = Utils.mapToVoidHttpEntityWithCookie(responseLogin.getHeaders());
		ResponseEntity<Object> response = restTemplate.exchange(URL + "/0", HttpMethod.GET, httpEntity, Object.class);

		assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
	}

	@Test
	public void test_findAll_for_admin_login() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		RequestUser requestRegister = new RequestUser().setUsername("admin").setPassword("12345").setUserType(UserType.ADMIN);
		restTemplate.exchange(URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);
		
		RequestAuth request = new RequestAuth().setUsername("admin").setPassword("12345");
		ResponseEntity<Void> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", request, Void.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

		HttpEntity<String> httpEntity = Utils.mapToVoidHttpEntityWithCookie(responseLogin.getHeaders());
		ResponseEntity<Object> response = restTemplate.exchange(URL + "/0", HttpMethod.GET, httpEntity, Object.class);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
}
