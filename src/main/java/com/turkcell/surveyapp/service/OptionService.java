package com.turkcell.surveyapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.turkcell.surveyapp.config.UserSession;
import com.turkcell.surveyapp.enumeration.UserType;
import com.turkcell.surveyapp.model.dto.OptionDTO;
import com.turkcell.surveyapp.util.SurveyMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptionService {
	private final OptionEntityService service;
	private final SurveyMapper mapper;
	private final UserSession userSession;
	private final UserOptionService userOptionService;

	public List<OptionDTO> save(OptionDTO dto) {
		return mapper.mapOptionDtoList(service.save(mapper.mapDtoToEntity(dto)));
	}
	
	public List<OptionDTO> saveAll(List<OptionDTO> list) {
		return mapper.mapOptionDtoList(service.saveAll(mapper.mapOptionEntityList(list)));
	}

	public OptionDTO findById(Long id) {
		if (UserType.USER.equals(userSession.getUser().getUserType())) {
			return mapper.mapEntityToDto(service.findByIdForUser(id).get());
		}
		return mapper.mapEntityToDto(service.findById(id).get());
	}
	
	public List<OptionDTO> findByQuestionId(Long questionId) {
		if (UserType.USER.equals(userSession.getUser().getUserType())) {
			return mapper.mapOptionDtoList(service.findByQuestionIdForUser(questionId));
		}
		return mapper.mapOptionDtoList(service.findByQuestionId(questionId));
	}
	
	public void select(Long optionId) {
		service.select(mapper.mapDtoToEntity(userSession.getUser()), optionId);
	}

	public List<OptionDTO> activateAll(Long questionId) {
		return mapper.mapOptionDtoList(service.activateAll(questionId));
	}

	public List<OptionDTO> activate(Long optionId) {
		return mapper.mapOptionDtoList(service.activate(optionId));
	}
	
	public Long getUserOptionIdByQuestionId(Long questionId) {
		return userOptionService.findByUsernameAndQuestionId(userSession.getUserName(), questionId);
	}
}
