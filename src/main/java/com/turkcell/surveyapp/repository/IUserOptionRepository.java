package com.turkcell.surveyapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.turkcell.surveyapp.entity.UserOptionEntity;

@Repository
public interface IUserOptionRepository extends JpaRepository<UserOptionEntity, Long> {
	@Query(value = "SELECT * FROM survey_user_option uo WHERE uo.fk_username = :username and uo.fk_survey_id = :surveyId", nativeQuery = true)
	List<UserOptionEntity> findByUsernameAndSurveyIdNative(@Param("username") String username, @Param("surveyId") Long surveyId);
	
	@Query(value = "SELECT * FROM survey_user_option uo WHERE uo.fk_username = :username and uo.fk_question_id = :questionId", nativeQuery = true)
	Optional<UserOptionEntity> findByUsernameAndQuestionIdNative(@Param("username") String username, @Param("questionId") Long questionId);
}
