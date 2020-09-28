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
public class OptionDTO extends BaseDTO {
	private Long id;
	private String option;
	private Integer sequence;
	private Status status = Status.PASSIVE;
	private Integer responseCount = 0;
	private QuestionDTO question;
}
