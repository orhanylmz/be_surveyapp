package com.turkcell.surveyapp.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.turkcell.surveyapp.entity.CategoryEntity;
import com.turkcell.surveyapp.entity.SurveyEntity;
import com.turkcell.surveyapp.repository.ISurveyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SurveyEntityService {
	private final ISurveyRepository repository;

	public SurveyEntity save(SurveyEntity entity) {
		return repository.save(entity);
	}

	public Optional<SurveyEntity> findById(Long id) {
		return repository.findById(id);
	}

	public Page<SurveyEntity> findAllByCategory(CategoryEntity category, Pageable pageable) {
		return repository.findAllByCategory(category, pageable);
	}
	
	public Page<SurveyEntity> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}
}
