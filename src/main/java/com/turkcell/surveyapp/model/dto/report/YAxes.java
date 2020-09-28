package com.turkcell.surveyapp.model.dto.report;

import lombok.Getter;

@Getter
public class YAxes {
	private GridLines gridLines = new GridLines();
	private Ticks ticks = new Ticks();
}
