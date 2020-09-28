package com.turkcell.surveyapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.turkcell.surveyapp.entity.OptionEntity;
import com.turkcell.surveyapp.entity.UserEntity;
import com.turkcell.surveyapp.entity.UserOptionEntity;
import com.turkcell.surveyapp.repository.IUserOptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserOptionEntityService {
	private final IUserOptionRepository repository;

	public UserOptionEntity save(UserOptionEntity entity) {
		return repository.save(entity);
	}

	public List<UserOptionEntity> findByUsernameAndSurveyId(String username, Long surveyId) {
		return repository.findByUsernameAndSurveyIdNative(username, surveyId);
	}

	public Long findByUsernameAndQuestionId(String username, Long questionId) {
		Optional<UserOptionEntity> userOptionOptional = repository.findByUsernameAndQuestionIdNative(username, questionId);
		return !userOptionOptional.isPresent() ? 0 : userOptionOptional.get().getOption().getId();
	}
	
	public UserOptionEntity save(UserEntity userEntity, OptionEntity optionEntity) {
		UserOptionEntity userOptionEntity = new UserOptionEntity()
				.setUser(userEntity)
				.setOption(optionEntity)
				.setQuestion(optionEntity.getQuestion())
				.setSurvey(optionEntity.getQuestion().getSurvey());
		return repository.save(userOptionEntity);
	}
}
