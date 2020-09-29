package com.turkcell.surveyapp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.turkcell.surveyapp.config.UserSession;
import com.turkcell.surveyapp.enumeration.UserType;
import com.turkcell.surveyapp.model.dto.OptionDTO;
import com.turkcell.surveyapp.model.dto.QuestionDTO;
import com.turkcell.surveyapp.model.dto.SurveyDTO;
import com.turkcell.surveyapp.model.dto.report.DataBar;
import com.turkcell.surveyapp.model.dto.report.Dataset;
import com.turkcell.surveyapp.model.dto.report.Report;
import com.turkcell.surveyapp.util.SurveyMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final SurveyMapper mapper;
	private final QuestionEntityService service;
	private final SurveyService surveyService;
	private final OptionService optionService;
	private final UserSession userSession;

	public QuestionDTO save(QuestionDTO dto) {

		return mapper.mapEntityToDto(service.save(mapper.mapDtoToEntity(dto)));
	}

	public QuestionDTO findById(Long id) {
		return mapper.mapEntityToDto(service.findById(id).get());
	}

	public List<QuestionDTO> findBySurveyId(Long surveyId) {
		SurveyDTO survey = surveyService.findById(surveyId);
		if (UserType.USER.equals(userSession.getUser().getUserType())) {
			return mapper.mapQuestionDtoList(service.findBySurveyForUser(mapper.mapDtoToEntity(survey)));
		}
		return mapper.mapQuestionDtoList(service.findBySurvey(mapper.mapDtoToEntity(survey)));
	}

	public QuestionDTO activate(Long id) {
		optionService.activateAll(id);
		return mapper.mapEntityToDto(service.activate(id));
	}

	public Report generateReportByQuestionId(Long questionId) {
		Report report = new Report();
		report.setDataBar(new DataBar());
		report.getDataBar().setLabels(new ArrayList<String>());
		report.getDataBar().setDatasets(Arrays.asList(new Dataset()));
		report.getDataBar().getDatasets().get(0).setLabel("% of Votes");
		report.getDataBar().getDatasets().get(0).setBorderWidth(2);

		report.getDataBar().getDatasets().get(0).setData(new ArrayList<Integer>());
		report.getDataBar().getDatasets().get(0).setBackgroundColor(new ArrayList<String>());
		report.getDataBar().getDatasets().get(0).setBorderColor(new ArrayList<String>());
		List<OptionDTO> optionList = optionService.findByQuestionId(questionId);

		for (int i = 0; i < optionList.size(); ++i) {
			report.getDataBar().getLabels().add(optionList.get(i).getOption());
			report.getDataBar().getDatasets().get(0).getData().add(optionList.get(i).getResponseCount());
			String rgba = generateRandomRGBA();
			report.getDataBar().getDatasets().get(0).getBackgroundColor().add(rgba);
			report.getDataBar().getDatasets().get(0).getBorderColor().add(rgba);
		}

		return report;
	}
	
	private String generateRandomRGBA() {
		Integer randomR = new Random().nextInt(255);
		Integer randomG = new Random().nextInt(255);
		Integer randomB = new Random().nextInt(255);
		return "rgba(" + randomR + ", " + randomG + "," + randomB + ",0.4)";
	}
}
