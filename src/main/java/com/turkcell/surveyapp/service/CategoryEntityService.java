package com.turkcell.surveyapp.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.turkcell.surveyapp.entity.CategoryEntity;
import com.turkcell.surveyapp.repository.ICategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryEntityService {
	private final ICategoryRepository repository;

	public CategoryEntity save(CategoryEntity entity) {
		return repository.save(entity);
	}

	public Optional<CategoryEntity> findById(Long id) {
		return repository.findById(id);
	}

	public Optional<CategoryEntity> findByName(String name) {
		return repository.findByName(name);
	}

	public Page<CategoryEntity> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}
}
