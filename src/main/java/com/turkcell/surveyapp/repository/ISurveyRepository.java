package com.turkcell.surveyapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.surveyapp.entity.CategoryEntity;
import com.turkcell.surveyapp.entity.SurveyEntity;

@Repository
public interface ISurveyRepository extends JpaRepository<SurveyEntity, Long> {
	Page<SurveyEntity> findAllByCategory(CategoryEntity category, Pageable pageable);
}
