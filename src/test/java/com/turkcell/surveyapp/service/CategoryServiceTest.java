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
import com.turkcell.surveyapp.model.dto.CategoryDTO;

@SpringBootTest
@EnableAutoConfiguration
@EnableAsync
public class CategoryServiceTest {
	@Autowired
	private CategoryService service;

	@Test
	public void test_save() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		CategoryDTO category = new CategoryDTO().setName("Category1");
		category = service.save(category);

		assertNotNull(category);
		assertNotNull(category.getId());
		assertThat(category.getName()).isEqualTo("Category1");
	}

	@Test
	public void test_findById() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		CategoryDTO category = new CategoryDTO().setName("Category2");
		category = service.save(category);

		CategoryDTO foundCategory = service.findById(category.getId());
		assertNotNull(foundCategory);
		assertNotNull(foundCategory.getId());
		assertThat(foundCategory.getName()).isEqualTo("Category2");
		assertThat(foundCategory.getId()).isEqualTo(category.getId());
	}

	@Test
	public void test_findByName() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		service.save(new CategoryDTO().setName("Category3"));

		CategoryDTO foundCategory = service.findByName("Category3");
		assertNotNull(foundCategory);
		assertNotNull(foundCategory.getId());
		assertThat(foundCategory.getName()).isEqualTo("Category3");
	}

	@Test
	public void test_findAll() throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException {
		service.save(new CategoryDTO().setName("Category4"));
		service.save(new CategoryDTO().setName("Category5"));
		service.save(new CategoryDTO().setName("Category6"));
		service.save(new CategoryDTO().setName("Category7"));

		Page<CategoryDTO> categoryList = service.findAll(PageRequest.of(0, Constants.PAGE_SIZE));

		assertNotNull(categoryList);
		assertNotNull(categoryList.getContent());
		assertThat(categoryList.getSize()).isEqualTo(Constants.PAGE_SIZE);
		assertThat(categoryList.getContent().size()).isGreaterThanOrEqualTo(4);
	}
}
