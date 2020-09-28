package com.turkcell.surveyapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

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
import com.turkcell.surveyapp.model.dto.UserDTO;
import com.turkcell.surveyapp.model.request.RequestAuth;
import com.turkcell.surveyapp.model.request.RequestCategory;
import com.turkcell.surveyapp.model.request.RequestUser;
import com.turkcell.surveyapp.service.CategoryService;
import com.turkcell.surveyapp.util.Utils;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@EnableAutoConfiguration
public class CategoryControllerTest {

	@Autowired
	private CategoryService categoryService;

	private String USER_URL = "http://localhost:8080/v1/user";
	private String URL = "http://localhost:8080/v1/category";
	private String AUTH_URL = "http://localhost:8080/v1/auth";

	private TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void test_save_user() throws RestClientException, JsonProcessingException {
		RequestUser requestRegister = new RequestUser().setUsername("user").setPassword("12345").setUserType(UserType.USER);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);
		
		RequestAuth requestAuth = new RequestAuth().setUsername("user").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

		CategoryDTO category = new CategoryDTO().setName("Category1");
		RequestCategory request = new RequestCategory().setCategory(category);

		ResponseEntity<CategoryDTO> response = restTemplate.exchange(URL, HttpMethod.PUT, Utils.mapToHttpEntityWithCookie(request, responseLogin.getHeaders()), CategoryDTO.class);

		assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
	}

	@Test
	public void test_save_admin() throws RestClientException, JsonProcessingException {
		RequestUser requestRegister = new RequestUser().setUsername("admin").setPassword("12345").setUserType(UserType.ADMIN);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);
		
		RequestAuth requestAuth = new RequestAuth().setUsername("admin").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

		CategoryDTO category = new CategoryDTO().setName("Category1");
		RequestCategory request = new RequestCategory().setCategory(category);

		ResponseEntity<CategoryDTO> response = restTemplate.exchange(URL, HttpMethod.PUT, Utils.mapToHttpEntityWithCookie(request, responseLogin.getHeaders()), CategoryDTO.class);

		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		assertNotNull(response.getBody().getId());
		assertEquals(response.getBody().getName(), "Category1");
	}

	@Test
	public void test_findAll_not_login() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		ResponseEntity<Object> response = restTemplate.getForEntity(URL + "/0", Object.class);

		assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void test_findAll() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		categoryService.save(new CategoryDTO().setName("Category1"));
		categoryService.save(new CategoryDTO().setName("Category2"));
		categoryService.save(new CategoryDTO().setName("Category3"));
		
		RequestUser requestRegister = new RequestUser().setUsername("user").setPassword("12345").setUserType(UserType.USER);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);

		RequestAuth requestAuth = new RequestAuth().setUsername("user").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

		ResponseEntity<Object> response = restTemplate.exchange(URL + "/0", HttpMethod.GET, Utils.mapToVoidHttpEntityWithCookie(responseLogin.getHeaders()), Object.class);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void test_findById() throws RestClientException, JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		CategoryDTO category = new CategoryDTO().setName("Category1");
		category = categoryService.save(category);
		
		RequestUser requestRegister = new RequestUser().setUsername("user").setPassword("12345").setUserType(UserType.USER);
		restTemplate.exchange(USER_URL, HttpMethod.PUT, Utils.mapToHttpEntity(requestRegister), UserDTO.class);

		RequestAuth requestAuth = new RequestAuth().setUsername("user").setPassword("12345");
		ResponseEntity<UserDTO> responseLogin = restTemplate.postForEntity(AUTH_URL + "/login", requestAuth, UserDTO.class);
		assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);

		ResponseEntity<CategoryDTO> response = restTemplate.exchange(URL + "/id/" + category.getId(), HttpMethod.GET, Utils.mapToVoidHttpEntityWithCookie(responseLogin.getHeaders()), CategoryDTO.class);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getName(), "Category1");
	}
}
