package com.turkcell.surveyapp.model.dto.report;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public class Scales {
	private List<XAxes> xAxes = Arrays.asList(new XAxes());
	private List<YAxes> yAxes = Arrays.asList(new YAxes());
}
