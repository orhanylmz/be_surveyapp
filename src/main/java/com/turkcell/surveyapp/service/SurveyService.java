package com.turkcell.surveyapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.turkcell.surveyapp.config.UserSession;
import com.turkcell.surveyapp.model.dto.CategoryDTO;
import com.turkcell.surveyapp.model.dto.SurveyDTO;
import com.turkcell.surveyapp.model.dto.UserOptionDTO;
import com.turkcell.surveyapp.util.SurveyMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SurveyService {
	private final SurveyEntityService service;
	private final CategoryService categoryService;
	private final UserOptionService userOptionService;
	private final SurveyMapper mapper;
	private final UserSession userSession;

	public SurveyDTO save(SurveyDTO dto) {
		return mapper.mapEntityToDto(service.save(mapper.mapDtoToEntity(dto)));
	}

	public SurveyDTO findById(Long id) {
		return mapper.mapEntityToDto(service.findById(id).get());
	}

	public Page<SurveyDTO> findAllByCategoryId(Long categoryId, Pageable pageable) {
		CategoryDTO category = categoryService.findById(categoryId);
		return mapper.mapSurveyPage(service.findAllByCategory(mapper.mapDtoToEntity(category), pageable));
	}

	public Page<SurveyDTO> findAll(Pageable pageable) {
		return mapper.mapSurveyPage(service.findAll(pageable));
	}
	
	public List<UserOptionDTO> findUserOptionsBySurveyId(Long surveyId) {
		return userOptionService.findByUsernameAndSurveyId(userSession.getUserName(), surveyId);
	}
}
