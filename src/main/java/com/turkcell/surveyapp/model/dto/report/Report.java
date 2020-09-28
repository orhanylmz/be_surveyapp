package com.turkcell.surveyapp.model.dto.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Report {
	private DataBar dataBar;
	private BarChartOptions barChartOptions = new BarChartOptions();
}
