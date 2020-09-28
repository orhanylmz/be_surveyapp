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
import com.turkcell.surveyapp.enumeration.Status;
import com.turkcell.surveyapp.enumeration.UserType;
import com.turkcell.surveyapp.model.dto.CategoryDTO;
import com.turkcell.surveyapp.model.dto.OptionDTO;
import com.turkcell.surveyapp.model.dto.QuestionDTO;
import com.turkcell.surveyapp.model.dto.SurveyDTO;
import com.turkcell.surveyapp.model.dto.UserDTO;

@SpringBootTest
@EnableAutoConfiguration
@EnableAsync
public class OptionServiceTest {
	@Autowired
	private OptionService service;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SurveyService surveyService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthService authService;

	private QuestionDTO question01;
	private QuestionDTO question02;

	@PostConstruct
	public void before() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		CategoryDTO category01 = categoryService.save(new CategoryDTO().setName("Category01"));

		SurveyDTO survey01 = surveyService.save(new SurveyDTO().setCategory(category01).setName("Survey1").setDescription("Survey1Description"));

		question01 = questionService.save(new QuestionDTO().setQuestion("Question1").setSequence(0).setSurvey(survey01));
		question02 = questionService.save(new QuestionDTO().setQuestion("Question2").setSequence(1).setSurvey(survey01));

		userService.save(new UserDTO().setUsername("username").setPassword("12345").setUserType(UserType.ADMIN));
		authService.login("username", "12345");
	}

	@Test
	public void test_save() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		List<OptionDTO> allOptionList = service.save(new OptionDTO().setOption("Option1").setQuestion(question01).setSequence(0));

		assertNotNull(allOptionList);
		assertThat(allOptionList.size()).isEqualTo(1);
		assertNotNull(allOptionList.get(0).getId());
		assertThat(allOptionList.get(0).getOption()).isEqualTo("Option1");
		assertThat(allOptionList.get(0).getQuestion().getId()).isEqualTo(question01.getId());
		assertThat(allOptionList.get(0).getSequence()).isEqualTo(0);
		assertThat(allOptionList.get(0).getResponseCount()).isEqualTo(0);

		allOptionList = service.save(new OptionDTO().setOption("Option2").setQuestion(question01).setSequence(1));

		assertNotNull(allOptionList);
		assertThat(allOptionList.size()).isEqualTo(2);
		assertNotNull(allOptionList.get(0).getId());
		assertThat(allOptionList.get(1).getOption()).isEqualTo("Option2");
		assertThat(allOptionList.get(1).getQuestion().getId()).isEqualTo(question01.getId());
		assertThat(allOptionList.get(1).getSequence()).isEqualTo(1);
		assertThat(allOptionList.get(1).getResponseCount()).isEqualTo(0);
	}

	@Test
	public void test_saveAll() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		List<OptionDTO> optionList = new ArrayList<>();
		optionList.add(new OptionDTO().setOption("Option3").setQuestion(question01).setSequence(0));
		optionList.add(new OptionDTO().setOption("Option4").setQuestion(question01).setSequence(1));
		optionList.add(new OptionDTO().setOption("Option5").setQuestion(question01).setSequence(2));
		optionList.add(new OptionDTO().setOption("Option6").setQuestion(question01).setSequence(3));

		List<OptionDTO> allOptionList = service.saveAll(optionList);

		assertNotNull(allOptionList);
		assertThat(allOptionList.size()).isEqualTo(4);
		assertNotNull(allOptionList.get(0).getId());
		assertThat(allOptionList.get(0).getOption()).isEqualTo("Option3");
		assertThat(allOptionList.get(0).getQuestion().getId()).isEqualTo(question01.getId());
		assertThat(allOptionList.get(0).getSequence()).isEqualTo(0);
		assertThat(allOptionList.get(0).getResponseCount()).isEqualTo(0);
	}

	@Test
	public void test_findById() {
		List<OptionDTO> allOptionList = service.save(new OptionDTO().setOption("Option7").setQuestion(question01).setSequence(0));
		assertNotNull(allOptionList);
		assertThat(allOptionList.size()).isEqualTo(1);

		OptionDTO foundOption = service.findById(allOptionList.get(0).getId());
		assertNotNull(foundOption);
		assertNotNull(foundOption.getId());
		assertThat(foundOption.getOption()).isEqualTo("Option7");
		assertThat(foundOption.getQuestion().getId()).isEqualTo(question01.getId());
		assertThat(foundOption.getSequence()).isEqualTo(0);
	}

	@Test
	public void test_findByQuestionId() {
		List<OptionDTO> optionList = new ArrayList<>();
		optionList.add(new OptionDTO().setOption("Option3").setQuestion(question01).setSequence(0));
		optionList.add(new OptionDTO().setOption("Option4").setQuestion(question01).setSequence(1));
		optionList.add(new OptionDTO().setOption("Option5").setQuestion(question01).setSequence(2));
		optionList.add(new OptionDTO().setOption("Option6").setQuestion(question01).setSequence(3));

		service.saveAll(optionList);

		List<OptionDTO> foundOptionList = service.findByQuestionId(question01.getId());
		assertNotNull(foundOptionList);
		assertThat(foundOptionList.size()).isEqualTo(4);
		assertThat(foundOptionList.get(0).getOption()).isEqualTo("Option3");
		assertThat(foundOptionList.get(0).getQuestion().getId()).isEqualTo(question01.getId());
		assertThat(foundOptionList.get(0).getResponseCount()).isEqualTo(0);
		assertThat(foundOptionList.get(0).getSequence()).isEqualTo(0);
		assertThat(foundOptionList.get(2).getSequence()).isEqualTo(2);
	}

	@Test
	public void test_select() {
		List<OptionDTO> optionList = new ArrayList<>();
		optionList.add(new OptionDTO().setOption("Option7").setQuestion(question01).setSequence(0));
		optionList.add(new OptionDTO().setOption("Option8").setQuestion(question01).setSequence(1));
		optionList.add(new OptionDTO().setOption("Option9").setQuestion(question01).setSequence(2));
		optionList.add(new OptionDTO().setOption("Option10").setQuestion(question01).setSequence(3));

		optionList = service.saveAll(optionList);

		OptionDTO option = service.findById(optionList.get(1).getId());
		assertNotNull(option);
		assertNotNull(option.getId());
		assertThat(option.getOption()).isEqualTo("Option8");
		assertThat(option.getQuestion().getId()).isEqualTo(question01.getId());
		assertThat(option.getSequence()).isEqualTo(1);
		assertThat(option.getResponseCount()).isEqualTo(0);

		service.select(optionList.get(1).getId());

		OptionDTO selectedOption = service.findById(optionList.get(1).getId());
		assertNotNull(selectedOption);
		assertNotNull(selectedOption.getId());
		assertThat(selectedOption.getOption()).isEqualTo("Option8");
		assertThat(selectedOption.getQuestion().getId()).isEqualTo(question01.getId());
		assertThat(selectedOption.getSequence()).isEqualTo(1);
		assertThat(selectedOption.getResponseCount()).isEqualTo(1);
	}

	@Test
	public void test_activeAll() {
		List<OptionDTO> optionList = new ArrayList<>();
		optionList.add(new OptionDTO().setOption("Option11").setQuestion(question02).setSequence(0));
		optionList.add(new OptionDTO().setOption("Option12").setQuestion(question02).setSequence(1));
		optionList.add(new OptionDTO().setOption("Option13").setQuestion(question02).setSequence(2));
		optionList.add(new OptionDTO().setOption("Option14").setQuestion(question02).setSequence(3));
		
		optionList = service.saveAll(optionList);
		assertNotNull(optionList);
		assertThat(optionList.size()).isEqualTo(4);
		assertThat(optionList.get(0).getStatus()).isEqualTo(Status.PASSIVE);
		assertThat(optionList.get(1).getStatus()).isEqualTo(Status.PASSIVE);
		
		List<OptionDTO> activatedOptionList = service.activateAll(question02.getId());
		
		assertNotNull(activatedOptionList);
		assertThat(activatedOptionList.size()).isEqualTo(4);
		assertThat(activatedOptionList.get(0).getStatus()).isEqualTo(Status.ACTIVE);
		assertThat(activatedOptionList.get(1).getStatus()).isEqualTo(Status.ACTIVE);
	}
	
	@Test
	public void test_active() {
		List<OptionDTO> optionList = new ArrayList<>();
		optionList.add(new OptionDTO().setOption("Option15").setQuestion(question02).setSequence(0));
		optionList.add(new OptionDTO().setOption("Option16").setQuestion(question02).setSequence(1));
		optionList.add(new OptionDTO().setOption("Option17").setQuestion(question02).setSequence(2));
		optionList.add(new OptionDTO().setOption("Option18").setQuestion(question02).setSequence(3));

		optionList = service.saveAll(optionList);
		assertNotNull(optionList);
		assertThat(optionList.size()).isEqualTo(4);
		assertThat(optionList.get(0).getStatus()).isEqualTo(Status.PASSIVE);
		assertThat(optionList.get(1).getStatus()).isEqualTo(Status.PASSIVE);

		List<OptionDTO> activatedOptionList = service.activate(optionList.get(0).getId());
		activatedOptionList = service.findByQuestionId(question02.getId());

		assertNotNull(activatedOptionList);
		assertThat(activatedOptionList.size()).isEqualTo(4);
		assertThat(activatedOptionList.get(0).getStatus()).isEqualTo(Status.ACTIVE);
		assertThat(activatedOptionList.get(1).getStatus()).isEqualTo(Status.PASSIVE);
	}
}
