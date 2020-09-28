package com.turkcell.surveyapp.model.dto.report;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataBar {
	private List<String> labels;
	private List<Dataset> datasets;
}
