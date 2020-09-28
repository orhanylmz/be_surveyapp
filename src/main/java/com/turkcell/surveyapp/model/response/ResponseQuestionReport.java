package com.turkcell.surveyapp.model.response;

import java.util.List;

import com.turkcell.surveyapp.model.dto.OptionDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ResponseQuestionReport {
	List<OptionDTO> list;
	private Long selectedOptionId;
}
