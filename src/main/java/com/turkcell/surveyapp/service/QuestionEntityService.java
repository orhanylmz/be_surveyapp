package com.turkcell.surveyapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.turkcell.surveyapp.entity.QuestionEntity;
import com.turkcell.surveyapp.entity.SurveyEntity;
import com.turkcell.surveyapp.enumeration.Status;
import com.turkcell.surveyapp.exception.EntityNotFoundException;
import com.turkcell.surveyapp.repository.IQuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionEntityService {
	private final IQuestionRepository repository;

	public QuestionEntity save(QuestionEntity entity) {
		if (entity.getSequence() != null) {
			return repository.save(entity);
		}
		Integer nextSequence = repository.nextSequenceNumberNative(entity.getSurvey().getId());
		if (nextSequence == null) {
			entity.setSequence(0);
		} else {
			entity.setSequence(++nextSequence);
		}
		return repository.save(entity);
	}

	public Optional<QuestionEntity> findById(Long id) {
		return repository.findById(id);
	}

	public List<QuestionEntity> findBySurvey(SurveyEntity survey) {
		return repository.findBySurveyOrderBySequenceAsc(survey);
	}

	public List<QuestionEntity> findBySurveyForUser(SurveyEntity survey) {
		return repository.findBySurveyAndStatusOrderBySequenceAsc(survey, Status.ACTIVE);
	}

	public QuestionEntity activate(Long questionId) {
		QuestionEntity question = findById(questionId).orElseThrow(EntityNotFoundException::new);
		question.setStatus(Status.ACTIVE);
		return save(question);
	}
}
