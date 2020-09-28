package com.turkcell.surveyapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.turkcell.surveyapp.model.dto.UserOptionDTO;
import com.turkcell.surveyapp.util.SurveyMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserOptionService {
	private final UserOptionEntityService service;
	private final SurveyMapper mapper;

	public List<UserOptionDTO> findByUsernameAndSurveyId(String username, Long surveyId) {
		return mapper.mapUserOptionDtoList(service.findByUsernameAndSurveyId(username, surveyId));
	}

	public Long findByUsernameAndQuestionId(String username, Long questionId) {
		return service.findByUsernameAndQuestionId(username, questionId);
	}
}
