package com.turkcell.surveyapp.model.request;

import java.util.List;

import com.turkcell.surveyapp.model.dto.OptionDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class RequestOption {
	OptionDTO option;
	List<OptionDTO> optionList;
}
