package com.turkcell.surveyapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.turkcell.surveyapp.enumeration.UserType;
import com.turkcell.surveyapp.model.dto.CategoryDTO;
import com.turkcell.surveyapp.model.dto.UserDTO;
import com.turkcell.surveyapp.service.CategoryService;
import com.turkcell.surveyapp.service.UserService;

@SpringBootApplication
public class SurveyappApplication implements CommandLineRunner {
	@Autowired
	private UserService userService;
	
	@Autowired
    private CategoryService categoryService;

	public static void main(String[] args) {
		SpringApplication.run(SurveyappApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		userService.saveNotLogin(new UserDTO().setUsername("admin").setPassword("12345").setUserType(UserType.ADMIN));
		userService.saveNotLogin(new UserDTO().setUsername("user").setPassword("12345").setUserType(UserType.USER));
		
		categoryService.save(new CategoryDTO().setName("DEFAULT"));
	}

}
