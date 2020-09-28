package com.turkcell.surveyapp.model.request;

import com.turkcell.surveyapp.model.dto.SurveyDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class RequestSurvey {
	SurveyDTO survey;
}
