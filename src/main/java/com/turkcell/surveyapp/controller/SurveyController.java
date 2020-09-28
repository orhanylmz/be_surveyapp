package com.turkcell.surveyapp.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.turkcell.surveyapp.config.Constants;
import com.turkcell.surveyapp.intf.RequiredAdmin;
import com.turkcell.surveyapp.intf.RequiredLogin;
import com.turkcell.surveyapp.model.dto.SurveyDTO;
import com.turkcell.surveyapp.model.dto.UserOptionDTO;
import com.turkcell.surveyapp.model.request.RequestSurvey;
import com.turkcell.surveyapp.model.response.ResponsePage;
import com.turkcell.surveyapp.service.SurveyService;
import com.turkcell.surveyapp.util.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/survey")
@RequiredArgsConstructor
public class SurveyController {
	private final SurveyService surveyService;

	@RequiredAdmin
	@PutMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public SurveyDTO save(@RequestBody RequestSurvey request) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("save: " + Utils.objectToJsonString(request));
		return surveyService.save(request.getSurvey());
	}

	@RequiredLogin
	@GetMapping("/{page}")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponsePage<SurveyDTO> findAll(@PathVariable Integer page) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("findAll: page: " + page);
		return new ResponsePage<SurveyDTO>().setList(surveyService.findAll(PageRequest.of(page, Constants.PAGE_SIZE)));
	}
	
	@RequiredLogin
	@GetMapping("/categoryId/{categoryId}/{page}")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponsePage<SurveyDTO> findAllByCategoryId(@PathVariable Long categoryId, @PathVariable Integer page) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("findAllByCategoryId: categoryId: " + categoryId + " page: " + page);
		return new ResponsePage<SurveyDTO>().setList(surveyService.findAllByCategoryId(categoryId, PageRequest.of(page, Constants.PAGE_SIZE)));
	}

	@RequiredLogin
	@GetMapping("/id/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public SurveyDTO findById(@PathVariable Long id) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("findById: id: " + id);
		return surveyService.findById(id);
	}
	
	@RequiredLogin
	@GetMapping("/id/{id}/userOption")
	@ResponseStatus(value = HttpStatus.OK)
	public List<UserOptionDTO> findUserOptionsBySurveyId(@PathVariable Long id) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("findUserOptionsBySurveyId: id: " + id);
		return surveyService.findUserOptionsBySurveyId(id);
	}
}
