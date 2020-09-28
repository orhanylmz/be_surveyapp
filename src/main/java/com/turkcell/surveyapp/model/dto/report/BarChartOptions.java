package com.turkcell.surveyapp.model.dto.report;

import lombok.Getter;

@Getter
public class BarChartOptions {
	private Boolean responsive = Boolean.TRUE;
	private Boolean maintainAspectRatio = Boolean.FALSE;
	private Scales scales = new Scales();
}
