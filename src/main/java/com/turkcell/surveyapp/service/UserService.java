package com.turkcell.surveyapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.turkcell.surveyapp.config.UserSession;
import com.turkcell.surveyapp.model.dto.UserDTO;
import com.turkcell.surveyapp.util.SurveyMapper;
import com.turkcell.surveyapp.util.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserEntityService service;
	private final UserSession userSession;
	private final SurveyMapper mapper;

	public UserDTO save(UserDTO dto) {
		dto.setPassword(Utils.encodeWithMD5(dto.getPassword()));
		dto = mapper.mapEntityToDto(service.save(mapper.mapDtoToEntity(dto)));
		userSession.setUser(dto);
		return dto;
	}

	public UserDTO findByUsernameAndPassword(String username, String password) {
		password = Utils.encodeWithMD5(password);
		return mapper.mapEntityToDto(service.findByUsernameAndPassword(username, password).get());
	}

	public Page<UserDTO> findAll(Pageable pageable) {
		return mapper.mapUserPage(service.findAll(pageable));
	}
}
