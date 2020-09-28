package com.turkcell.surveyapp.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class CategoryDTO extends BaseDTO {
	private Long id;
	private String name;
}
