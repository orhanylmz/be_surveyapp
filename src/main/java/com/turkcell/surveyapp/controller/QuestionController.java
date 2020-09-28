package com.turkcell.surveyapp.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.turkcell.surveyapp.intf.RequiredAdmin;
import com.turkcell.surveyapp.intf.RequiredLogin;
import com.turkcell.surveyapp.model.dto.QuestionDTO;
import com.turkcell.surveyapp.model.dto.report.Report;
import com.turkcell.surveyapp.model.request.RequestQuestion;
import com.turkcell.surveyapp.model.response.ResponseList;
import com.turkcell.surveyapp.service.QuestionService;
import com.turkcell.surveyapp.util.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/question")
@RequiredArgsConstructor
public class QuestionController {
	private final QuestionService questionService;

	@RequiredLogin
	@PutMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public QuestionDTO save(@RequestBody RequestQuestion request) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("save: " + Utils.objectToJsonString(request));
		return questionService.save(request.getQuestion());
	}

	@RequiredLogin
	@GetMapping("/surveyId/{surveyId}/{page}")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseList<QuestionDTO> findAllBySurveyId(@PathVariable Long surveyId, @PathVariable Integer page) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("findAllBySurveyId: surveyId: " + surveyId + " page: " + page);
		return new ResponseList<QuestionDTO>().setList(questionService.findBySurveyId(surveyId));
	}

	@RequiredLogin
	@GetMapping("/id/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public QuestionDTO findById(@PathVariable Long id) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("findById: id: " + id);
		return questionService.findById(id);
	}
	
	@RequiredAdmin
	@GetMapping("/id/{id}/activate")
	@ResponseStatus(value = HttpStatus.OK)
	public QuestionDTO activate(@PathVariable Long id) {
		log.info("activate: id: " + id);
		return questionService.activate(id);
	}
	
	@RequiredAdmin
	@GetMapping("/id/{id}/report")
	@ResponseStatus(value = HttpStatus.OK)
	public Report generateReportByQuestionId(@PathVariable Long id) {
		log.info("report: id: " + id);
		return questionService.generateReportByQuestionId(id);
	}
}
