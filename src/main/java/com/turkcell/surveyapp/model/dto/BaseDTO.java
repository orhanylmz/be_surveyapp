package com.turkcell.surveyapp.model.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseDTO {
	LocalDateTime createdAt;
	String createdBy;
	LocalDateTime updatedAt;
	String updatedBy;
}
