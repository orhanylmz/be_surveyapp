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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableAsync;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.turkcell.surveyapp.config.Constants;
import com.turkcell.surveyapp.model.dto.CategoryDTO;
import com.turkcell.surveyapp.model.dto.SurveyDTO;

@SpringBootTest
@EnableAutoConfiguration
@EnableAsync
public class SurveyServiceTest {
	@Autowired
	private SurveyService service;

	@Autowired
	private CategoryService categoryService;

	private CategoryDTO category01;
	private CategoryDTO category02;

	@PostConstruct
	public void before() {
		category01 = categoryService.save(new CategoryDTO().setName("Category01"));
		category02 = categoryService.save(new CategoryDTO().setName("Category02"));
	}

	@Test
	public void test_save() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		SurveyDTO survey = new SurveyDTO().setCategory(category01).setName("Survey1").setDescription("Survey1Description");
		survey = service.save(survey);

		assertNotNull(survey);
		assertNotNull(survey.getId());
		assertThat(survey.getName()).isEqualTo("Survey1");
		assertThat(survey.getDescription()).isEqualTo("Survey1Description");
		assertThat(survey.getCategory().getId()).isEqualTo(category01.getId());
	}

	@Test
	public void test_findById() {
		SurveyDTO survey = new SurveyDTO().setCategory(category01).setName("Survey2").setDescription("Survey2Description");
		survey = service.save(survey);

		SurveyDTO foundSurvey = service.findById(survey.getId());
		assertNotNull(foundSurvey);
		assertNotNull(foundSurvey.getId());
		assertThat(foundSurvey.getName()).isEqualTo("Survey2");
		assertThat(foundSurvey.getDescription()).isEqualTo("Survey2Description");
		assertThat(foundSurvey.getCategory().getId()).isEqualTo(category01.getId());
	}

	@Test
	public void test_findAllByCategoryId() {
		service.save(new SurveyDTO().setCategory(category01).setName("Survey3").setDescription("Survey3Description"));
		service.save(new SurveyDTO().setCategory(category01).setName("Survey4").setDescription("Survey4Description"));
		service.save(new SurveyDTO().setCategory(category01).setName("Survey5").setDescription("Survey5Description"));
		service.save(new SurveyDTO().setCategory(category01).setName("Survey6").setDescription("Survey6Description"));
		service.save(new SurveyDTO().setCategory(category02).setName("Survey7").setDescription("Survey7Description"));

		Page<SurveyDTO> surveyList = service.findAllByCategoryId(category01.getId(), PageRequest.of(0, Constants.PAGE_SIZE));
		assertNotNull(surveyList);
		assertNotNull(surveyList.getContent());
		assertThat(surveyList.getSize()).isEqualTo(Constants.PAGE_SIZE);
		assertThat(surveyList.getContent().size()).isEqualTo(4);

		surveyList = service.findAllByCategoryId(category02.getId(), PageRequest.of(0, Constants.PAGE_SIZE));
		assertNotNull(surveyList);
		assertNotNull(surveyList.getContent());
		assertThat(surveyList.getSize()).isEqualTo(Constants.PAGE_SIZE);
		assertThat(surveyList.getContent().size()).isEqualTo(1);
	}

	@Test
	public void findAll() {
		service.save(new SurveyDTO().setCategory(category01).setName("Survey8").setDescription("Survey8Description"));
		service.save(new SurveyDTO().setCategory(category01).setName("Survey9").setDescription("Survey9Description"));
		service.save(new SurveyDTO().setCategory(category01).setName("Survey10").setDescription("Survey10Description"));
		service.save(new SurveyDTO().setCategory(category01).setName("Survey11").setDescription("Survey11Description"));
		service.save(new SurveyDTO().setCategory(category02).setName("Survey12").setDescription("Survey12Description"));

		Page<SurveyDTO> surveyList = service.findAll(PageRequest.of(0, Constants.PAGE_SIZE));
		assertNotNull(surveyList);
		assertNotNull(surveyList.getContent());
		assertThat(surveyList.getSize()).isEqualTo(Constants.PAGE_SIZE);
		assertThat(surveyList.getContent().size()).isGreaterThanOrEqualTo(5);
	}
}
