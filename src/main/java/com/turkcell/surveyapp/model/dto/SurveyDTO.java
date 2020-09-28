package com.turkcell.surveyapp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class SurveyDTO extends BaseDTO {
	private Long id;
	private String name;
	private String description;
	private CategoryDTO category;
}
