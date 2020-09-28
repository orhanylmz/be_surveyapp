package com.turkcell.surveyapp.util;

import java.util.List;

import javax.annotation.Priority;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.turkcell.surveyapp.entity.BaseEntity;
import com.turkcell.surveyapp.entity.CategoryEntity;
import com.turkcell.surveyapp.entity.OptionEntity;
import com.turkcell.surveyapp.entity.QuestionEntity;
import com.turkcell.surveyapp.entity.SurveyEntity;
import com.turkcell.surveyapp.entity.UserEntity;
import com.turkcell.surveyapp.entity.UserOptionEntity;
import com.turkcell.surveyapp.model.dto.BaseDTO;
import com.turkcell.surveyapp.model.dto.CategoryDTO;
import com.turkcell.surveyapp.model.dto.OptionDTO;
import com.turkcell.surveyapp.model.dto.QuestionDTO;
import com.turkcell.surveyapp.model.dto.SurveyDTO;
import com.turkcell.surveyapp.model.dto.UserDTO;
import com.turkcell.surveyapp.model.dto.UserOptionDTO;

@Priority(value = 0)
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class SurveyMapper {
	public abstract BaseDTO mapEntityToDto(BaseEntity entity);

	public abstract BaseEntity mapDtoToEntity(BaseDTO dto);

	public abstract CategoryDTO mapEntityToDto(CategoryEntity entity);

	public abstract CategoryEntity mapDtoToEntity(CategoryDTO dto);

	public Page<CategoryDTO> mapCategoryPage(Page<CategoryEntity> page) {
		return page.map(this::mapEntityToDto);
	}

	public abstract List<CategoryDTO> mapCategoryDtoList(List<CategoryEntity> list);
	
	public abstract List<CategoryEntity> mapCategoryEntityList(List<CategoryDTO> list);

	public abstract OptionDTO mapEntityToDto(OptionEntity entity);

	public abstract OptionEntity mapDtoToEntity(OptionDTO dto);

	public Page<OptionDTO> mapOptionPage(Page<OptionEntity> page) {
		return page.map(this::mapEntityToDto);
	}

	public abstract List<OptionDTO> mapOptionDtoList(List<OptionEntity> list);
	
	public abstract List<OptionEntity> mapOptionEntityList(List<OptionDTO> list);
	
	public abstract QuestionDTO mapEntityToDto(QuestionEntity entity);

	public abstract QuestionEntity mapDtoToEntity(QuestionDTO dto);

	public Page<QuestionDTO> mapQuestionPage(Page<QuestionEntity> page) {
		return page.map(this::mapEntityToDto);
	}

	public abstract List<QuestionDTO> mapQuestionDtoList(List<QuestionEntity> list);
	
	public abstract List<QuestionEntity> mapQuestionEntityList(List<QuestionDTO> list);

	public abstract SurveyDTO mapEntityToDto(SurveyEntity entity);

	public abstract SurveyEntity mapDtoToEntity(SurveyDTO dto);

	public Page<SurveyDTO> mapSurveyPage(Page<SurveyEntity> page) {
		return page.map(this::mapEntityToDto);
	}

	public abstract List<SurveyDTO> mapSurveyDtoList(List<SurveyEntity> list);
	
	public abstract List<SurveyEntity> mapSurveyEntityList(List<SurveyDTO> list);
	
	public abstract UserDTO mapEntityToDto(UserEntity entity);

	public abstract UserEntity mapDtoToEntity(UserDTO dto);

	public Page<UserDTO> mapUserPage(Page<UserEntity> page) {
		return page.map(this::mapEntityToDto);
	}

	public abstract List<UserDTO> mapUserDtoList(List<UserEntity> list);
	
	public abstract List<UserEntity> mapUserEntityList(List<UserDTO> list);
	
	public abstract UserOptionDTO mapEntityToDto(UserOptionEntity entity);
	
	public abstract List<UserOptionDTO> mapUserOptionDtoList(List<UserOptionEntity> list);


}
