package com.turkcell.surveyapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.turkcell.surveyapp.config.UserSession;
import com.turkcell.surveyapp.enumeration.UserType;
import com.turkcell.surveyapp.model.dto.UserDTO;

@SpringBootTest
@EnableAutoConfiguration
@EnableAsync
public class AuthServiceTest {
	@Autowired
	private AuthService service;

	@Autowired
	private UserService userService;

	@Autowired
	private UserSession userSession;

	@PostConstruct
	public void before() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		userService.save(new UserDTO().setUsername("user").setPassword("pass").setUserType(UserType.USER));
		userService.save(new UserDTO().setUsername("admin").setPassword("pass").setUserType(UserType.ADMIN));
	}

	@Test
	public void test_user_login() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		service.login("user", "pass");

		assertNotNull(userSession);
		assertNotNull(userSession.getUser());
		assertThat(userSession.getUser().getUsername()).isEqualTo("user");
		assertThat(userSession.getUser().getUserType()).isEqualTo(UserType.USER);
	}

	@Test
	public void test_admin_login() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		service.login("admin", "pass");

		assertNotNull(userSession);
		assertNotNull(userSession.getUser());
		assertThat(userSession.getUser().getUsername()).isEqualTo("admin");
		assertThat(userSession.getUser().getUserType()).isEqualTo(UserType.ADMIN);
	}

	@Test
	public void test_logout() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		service.login("admin", "pass");

		assertNotNull(userSession);
		assertNotNull(userSession.getUser());
		assertThat(userSession.getUser().getUsername()).isEqualTo("admin");
		assertThat(userSession.getUser().getUserType()).isEqualTo(UserType.ADMIN);

		service.logout();

		assertNotNull(userSession);
		assertNull(userSession.getUser());
	}
}
