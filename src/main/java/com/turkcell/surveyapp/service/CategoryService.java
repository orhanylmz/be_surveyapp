package com.turkcell.surveyapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.turkcell.surveyapp.model.dto.CategoryDTO;
import com.turkcell.surveyapp.util.SurveyMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryEntityService service;
	private final SurveyMapper mapper;

	public CategoryDTO save(CategoryDTO dto) {
		return mapper.mapEntityToDto(service.save(mapper.mapDtoToEntity(dto)));
	}

	public CategoryDTO findById(Long id) {
		return mapper.mapEntityToDto(service.findById(id).get());
	}

	public CategoryDTO findByName(String name) {
		return mapper.mapEntityToDto(service.findByName(name).get());
	}

	public Page<CategoryDTO> findAll(Pageable pageable) {
		return mapper.mapCategoryPage(service.findAll(pageable));
	}
}
