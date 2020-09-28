package com.turkcell.surveyapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.turkcell.surveyapp.enumeration.Status;
import com.turkcell.surveyapp.model.dto.CategoryDTO;
import com.turkcell.surveyapp.model.dto.QuestionDTO;
import com.turkcell.surveyapp.model.dto.SurveyDTO;

@SpringBootTest
@EnableAutoConfiguration
@EnableAsync
public class QuestionServiceTest {
	@Autowired
	private QuestionService service;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SurveyService surveyService;

	private SurveyDTO survey01;

	@PostConstruct
	public void before() {
		CategoryDTO category01 = categoryService.save(new CategoryDTO().setName("Category01"));

		survey01 = surveyService.save(new SurveyDTO().setCategory(category01).setName("Survey1").setDescription("Survey1Description"));
	}

	@Test
	public void test_save() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		QuestionDTO question = new QuestionDTO().setQuestion("Question1").setSequence(0).setSurvey(survey01);
		question = service.save(question);

		assertNotNull(question);
		assertNotNull(question.getId());
		assertThat(question.getQuestion()).isEqualTo("Question1");
		assertThat(question.getSequence()).isEqualTo(0);
		assertThat(question.getStatus()).isEqualTo(Status.PASSIVE);
		assertThat(question.getSurvey().getId()).isEqualTo(survey01.getId());
	}

	@Test
	public void test_findById() {
		QuestionDTO question = new QuestionDTO().setQuestion("Question2").setSequence(0).setSurvey(survey01);
		question = service.save(question);

		QuestionDTO foundQuestion = service.findById(question.getId());
		assertNotNull(foundQuestion);
		assertNotNull(foundQuestion.getId());
		assertThat(foundQuestion.getQuestion()).isEqualTo("Question2");
		assertThat(foundQuestion.getSequence()).isEqualTo(0);
		assertThat(foundQuestion.getStatus()).isEqualTo(Status.PASSIVE);
		assertThat(foundQuestion.getSurvey().getId()).isEqualTo(survey01.getId());
	}

	@Test
	public void test_activate() {
		QuestionDTO question = new QuestionDTO().setQuestion("Question8").setSequence(0).setSurvey(survey01);
		question = service.save(question);

		assertNotNull(question);
		assertNotNull(question.getId());
		assertThat(question.getQuestion()).isEqualTo("Question8");
		assertThat(question.getStatus()).isEqualTo(Status.PASSIVE);

		QuestionDTO activatedQuestion = service.activate(question.getId());
		assertNotNull(activatedQuestion);
		assertNotNull(activatedQuestion.getId());
		assertThat(activatedQuestion.getQuestion()).isEqualTo("Question8");
		assertThat(activatedQuestion.getStatus()).isEqualTo(Status.ACTIVE);
	}
}
