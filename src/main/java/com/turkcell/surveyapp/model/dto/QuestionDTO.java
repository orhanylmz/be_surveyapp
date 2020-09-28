package com.turkcell.surveyapp.model.dto;


import com.turkcell.surveyapp.enumeration.Status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class QuestionDTO extends BaseDTO {
	private Long id;
	private String question;
	private Integer sequence;
	private Status status = Status.PASSIVE;
	private SurveyDTO survey;
}
