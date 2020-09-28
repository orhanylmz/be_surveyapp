package com.turkcell.surveyapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.turkcell.surveyapp.enumeration.UserType;
import com.turkcell.surveyapp.model.dto.CategoryDTO;
import com.turkcell.surveyapp.model.dto.OptionDTO;
import com.turkcell.surveyapp.model.dto.QuestionDTO;
import com.turkcell.surveyapp.model.dto.SurveyDTO;
import com.turkcell.surveyapp.model.dto.UserDTO;
import com.turkcell.surveyapp.model.dto.UserOptionDTO;

@SpringBootTest
@EnableAutoConfiguration
@EnableAsync
public class UserOptionServiceTest {
	@Autowired
	private UserOptionService service;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SurveyService surveyService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private OptionService optionService;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthService authService;

	private SurveyDTO survey;
	private QuestionDTO question01;
	private QuestionDTO question02;
	private QuestionDTO question03;

	@PostConstruct
	public void before() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		CategoryDTO category01 = categoryService.save(new CategoryDTO().setName("Category01"));

		survey = surveyService.save(new SurveyDTO().setCategory(category01).setName("Survey1").setDescription("Survey1Description"));

		question01 = questionService.save(new QuestionDTO().setQuestion("Question1").setSequence(0).setSurvey(survey));

		List<OptionDTO> question1OptionList = new ArrayList<>();
		question1OptionList.add(new OptionDTO().setOption("Option1").setQuestion(question01).setSequence(0));
		question1OptionList.add(new OptionDTO().setOption("Option2").setQuestion(question01).setSequence(1));
		question1OptionList.add(new OptionDTO().setOption("Option3").setQuestion(question01).setSequence(2));
		question1OptionList.add(new OptionDTO().setOption("Option4").setQuestion(question01).setSequence(3));

		question1OptionList = optionService.saveAll(question1OptionList);

		question02 = questionService.save(new QuestionDTO().setQuestion("Question2").setSequence(1).setSurvey(survey));

		List<OptionDTO> question2OptionList = new ArrayList<>();
		question2OptionList.add(new OptionDTO().setOption("Option1").setQuestion(question02).setSequence(0));
		question2OptionList.add(new OptionDTO().setOption("Option2").setQuestion(question02).setSequence(1));
		question2OptionList.add(new OptionDTO().setOption("Option3").setQuestion(question02).setSequence(2));
		question2OptionList.add(new OptionDTO().setOption("Option4").setQuestion(question02).setSequence(3));

		question2OptionList = optionService.saveAll(question2OptionList);

		question03 = questionService.save(new QuestionDTO().setQuestion("Question3").setSequence(2).setSurvey(survey));

		List<OptionDTO> question3OptionList = new ArrayList<>();
		question3OptionList.add(new OptionDTO().setOption("Option1").setQuestion(question03).setSequence(0));
		question3OptionList.add(new OptionDTO().setOption("Option2").setQuestion(question03).setSequence(1));
		question3OptionList.add(new OptionDTO().setOption("Option3").setQuestion(question03).setSequence(2));
		question3OptionList.add(new OptionDTO().setOption("Option4").setQuestion(question03).setSequence(3));

		question3OptionList = optionService.saveAll(question3OptionList);

		userService.save(new UserDTO().setUsername("username").setPassword("12345").setUserType(UserType.ADMIN));
		authService.login("username", "12345");

		optionService.select(question1OptionList.get(1).getId());
		optionService.select(question2OptionList.get(3).getId());
	}

	@Test
	public void test_findByUsernameAndSurveyId() {
		List<UserOptionDTO> optionList = service.findByUsernameAndSurveyId("username", survey.getId());

		assertNotNull(optionList);
		assertThat(optionList.size()).isEqualTo(2);
		assertThat(optionList.get(0).getSurvey().getId()).isEqualTo(survey.getId());
	}

	@Test
	public void test_findByUsernameAndQuestionId() {
		Long optionId = service.findByUsernameAndQuestionId("username", question01.getId());

		assertNotNull(optionId);
		assertThat(optionId).isGreaterThan(0L);

		optionId = service.findByUsernameAndQuestionId("username", question02.getId());

		assertNotNull(optionId);
		assertThat(optionId).isGreaterThan(0L);

		optionId = service.findByUsernameAndQuestionId("username", question03.getId());

		assertNotNull(optionId);
		assertThat(optionId).isLessThanOrEqualTo(0L);
	}
}
