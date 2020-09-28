package com.turkcell.surveyapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.turkcell.surveyapp.entity.QuestionEntity;
import com.turkcell.surveyapp.entity.SurveyEntity;
import com.turkcell.surveyapp.enumeration.Status;

@Repository
public interface IQuestionRepository extends JpaRepository<QuestionEntity, Long> {
	List<QuestionEntity> findBySurveyOrderBySequenceAsc(SurveyEntity survey);
	
	List<QuestionEntity> findBySurveyAndStatusOrderBySequenceAsc(SurveyEntity survey, Status status);

	@Query(value = "SELECT max(q.sequence) FROM survey_question q where q.fk_survey_id = :surveyId", nativeQuery = true)
	Integer nextSequenceNumberNative(@Param("surveyId") Long surveyId);
}
