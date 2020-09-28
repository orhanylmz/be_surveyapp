package com.turkcell.surveyapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableAsync;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.turkcell.surveyapp.config.Constants;
import com.turkcell.surveyapp.enumeration.UserType;
import com.turkcell.surveyapp.model.dto.UserDTO;

@SpringBootTest
@EnableAutoConfiguration
@EnableAsync
public class UserServiceTest {
	@Autowired
	private UserService service;

	@Test
	public void test_save_for_admin() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		UserDTO user = new UserDTO().setUsername("user1").setPassword("pass").setUserType(UserType.ADMIN);
		user = service.save(user);

		assertNotNull(user);
		assertThat(user.getUsername()).isEqualTo("user1");
		assertThat(user.getPassword()).isNotEqualTo("pass");
		assertThat(user.getUserType()).isEqualTo(UserType.ADMIN);
	}

	@Test
	public void test_save_for_user() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		UserDTO user = new UserDTO().setUsername("user2").setPassword("pass").setUserType(UserType.USER);
		user = service.save(user);

		assertNotNull(user);
		assertThat(user.getUsername()).isEqualTo("user2");
		assertThat(user.getPassword()).isNotEqualTo("pass");
		assertThat(user.getUserType()).isEqualTo(UserType.USER);
	}

	@Test
	public void test_findByUsernameAndPassword() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		service.save(new UserDTO().setUsername("user3").setPassword("pass").setUserType(UserType.USER));

		UserDTO user = service.findByUsernameAndPassword("user3", "pass");

		assertNotNull(user);
		assertThat(user.getUsername()).isEqualTo("user3");
		assertThat(user.getPassword()).isNotEqualTo("pass");
		assertThat(user.getUserType()).isEqualTo(UserType.USER);
	}

	@Test
	public void test_findAll() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		service.save(new UserDTO().setUsername("user4").setPassword("pass").setUserType(UserType.USER));
		service.save(new UserDTO().setUsername("user5").setPassword("pass").setUserType(UserType.USER));
		service.save(new UserDTO().setUsername("user6").setPassword("pass").setUserType(UserType.USER));
		service.save(new UserDTO().setUsername("user7").setPassword("pass").setUserType(UserType.USER));

		Page<UserDTO> userList = service.findAll(PageRequest.of(0, Constants.PAGE_SIZE));

		assertNotNull(userList);
		assertNotNull(userList.getContent());
		assertThat(userList.getSize()).isEqualTo(Constants.PAGE_SIZE);
		assertThat(userList.getContent().size()).isGreaterThan(0);
	}
}
