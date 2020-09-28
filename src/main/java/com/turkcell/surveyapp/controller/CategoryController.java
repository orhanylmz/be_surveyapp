package com.turkcell.surveyapp.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

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
import com.turkcell.surveyapp.model.dto.CategoryDTO;
import com.turkcell.surveyapp.model.request.RequestCategory;
import com.turkcell.surveyapp.model.response.ResponsePage;
import com.turkcell.surveyapp.service.CategoryService;
import com.turkcell.surveyapp.util.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/category")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;

	@PutMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	@RequiredAdmin
	public CategoryDTO save(@RequestBody RequestCategory request) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("save: " + Utils.objectToJsonString(request));
		return categoryService.save(request.getCategory());
	}

	@RequiredLogin
	@GetMapping("/{page}")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponsePage<CategoryDTO> findAll(@PathVariable Integer page) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("findAll: page: " + page);
		return new ResponsePage<CategoryDTO>().setList(categoryService.findAll(PageRequest.of(page, Constants.PAGE_SIZE)));
	}

	@RequiredLogin
	@GetMapping("/id/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public CategoryDTO findById(@PathVariable Long id) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		log.info("findById: id: " + id);
		return categoryService.findById(id);
	}
}
