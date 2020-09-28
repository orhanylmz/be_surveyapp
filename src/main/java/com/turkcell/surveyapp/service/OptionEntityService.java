package com.turkcell.surveyapp.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.turkcell.surveyapp.entity.OptionEntity;
import com.turkcell.surveyapp.entity.UserEntity;
import com.turkcell.surveyapp.enumeration.Status;
import com.turkcell.surveyapp.exception.EntityNotFoundException;
import com.turkcell.surveyapp.repository.IOptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptionEntityService {
	private final IOptionRepository repository;
	private final UserOptionEntityService userOptionEntityService;

	public List<OptionEntity> save(OptionEntity entity) {
		repository.save(entity);
		return findByQuestionId(entity.getQuestion().getId());
	}
	
	public List<OptionEntity> saveAll(List<OptionEntity> entityList) {
		repository.saveAll(entityList);
		return findByQuestionId(entityList.get(0).getQuestion().getId());
	}
	
	public Optional<OptionEntity> findById(Long id) {
		return repository.findById(id);
	}
	
	public Optional<OptionEntity> findByIdForUser(Long id) {
		return repository.findByIdAndStatus(id, Status.ACTIVE);
	}

	public List<OptionEntity> findByQuestionId(Long questionId) {
		return repository.findByQuestionOrderBySequenceAscNative(questionId);
	}
	
	public List<OptionEntity> findByQuestionIdForUser(Long questionId) {
		return repository.findByQuestionAndStatusOrderBySequenceAscNative(questionId, Status.ACTIVE.name());
	}

	public void select(UserEntity user, Long optionId) {
		OptionEntity entity = repository.findById(optionId).orElseThrow(EntityNotFoundException::new);
		userOptionEntityService.save(user, entity);
		repository.increaseResponseCount(entity.getId());
	}

	public List<OptionEntity> activateAll(Long questionId) {
		List<OptionEntity> optionList = findByQuestionId(questionId);
		if (CollectionUtils.isEmpty(optionList)) {
			return Collections.emptyList();
		}
		for (OptionEntity option : optionList) {
			option.setStatus(Status.ACTIVE);
		}
		return saveAll(optionList);
	}

	public List<OptionEntity> activate(Long optionId) {
		OptionEntity option = findById(optionId).orElseThrow(EntityNotFoundException::new);
		option.setStatus(Status.ACTIVE);
		return save(option);
	}
}
