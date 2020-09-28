package com.turkcell.surveyapp.model.dto.report;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Dataset {
	private String label;
	private List<Integer> data;
	private List<String> backgroundColor;
	private Integer borderWidth;
	private List<String> borderColor;
}
