package com.turkcell.surveyapp.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
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
import com.turkcell.surveyapp.model.dto.OptionDTO;
import com.turkcell.surveyapp.model.request.RequestOption;
import com.turkcell.surveyapp.model.response.ResponseList;
import com.turkcell.surveyapp.model.response.ResponseOptionList;
import com.turkcell.surveyapp.service.OptionService;
import com.turkcell.surveyapp.util.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/option")
@RequiredArgsConstructor
public class OptionController {
	private final OptionService optionService;

	@RequiredLogin
	@PutMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseList<OptionDTO> save(@RequestBody RequestOption request) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("save: " + Utils.objectToJsonString(request));
		if (!CollectionUtils.isEmpty(request.getOptionList())) {
			return new ResponseList<OptionDTO>().setList(optionService.saveAll(request.getOptionList()));
		}
		return new ResponseList<OptionDTO>().setList(optionService.save(request.getOption()));
	}

	@RequiredLogin
	@GetMapping("/questionId/{questionId}")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseOptionList findAllByQuestionId(@PathVariable Long questionId) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("findAllByQuestionId: questionId: " + questionId);
		return new ResponseOptionList().setList(optionService.findByQuestionId(questionId)).setSelectedOptionId(optionService.getUserOptionIdByQuestionId(questionId));
	}

	@RequiredLogin
	@GetMapping("/id/{id}/select")
	@ResponseStatus(value = HttpStatus.OK)
	public Long select(@PathVariable Long id) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("select: optionId: " + id);
		optionService.select(id);
		return id;
	}

	@RequiredAdmin
	@GetMapping("/id/{id}/activate")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseList<OptionDTO> activate(@PathVariable Long id) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("select: optionId: " + id);
		return new ResponseList<OptionDTO>().setList(optionService.activate(id));
	}
}
