package com.turkcell.surveyapp.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.turkcell.surveyapp.exception.AccessDeniedException;
import com.turkcell.surveyapp.exception.InvalidCredentialException;
import com.turkcell.surveyapp.exception.SurveyException;
import com.turkcell.surveyapp.exception.UserNotLoginException;

@ControllerAdvice
public class SurveyContollerAdvice {
	@ExceptionHandler(InvalidCredentialException.class)
	public ResponseEntity<String> handleInvalidCredentialException(InvalidCredentialException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UserNotLoginException.class)
	public ResponseEntity<String> handleInvalidCredentialException(UserNotLoginException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(SurveyException.class)
	public ResponseEntity<String> handleSurveyException(SurveyException exception) {
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
