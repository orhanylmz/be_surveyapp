package com.turkcell.surveyapp.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.turkcell.surveyapp.entity.OptionEntity;
import com.turkcell.surveyapp.enumeration.Status;

@Repository
public interface IOptionRepository extends JpaRepository<OptionEntity, Long> {
	@Query(value = "SELECT * FROM survey_option o WHERE o.fk_question_id = :questionId order by o.sequence asc", nativeQuery = true)
	List<OptionEntity> findByQuestionOrderBySequenceAscNative(@Param("questionId") Long questionId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE survey_option o set o.response_count = (o.response_count + 1) where o.id = :optionId", nativeQuery = true)
	void increaseResponseCount(Long optionId);
	
	@Query(value = "SELECT max(q.sequence) FROM survey_option o where o.fk_question_id = :questionId", nativeQuery = true)
	Integer nextSequenceNumberNative(@Param("questionId") Long questionId);
	
	@Query(value = "SELECT * FROM survey_option o WHERE o.fk_question_id = :questionId and o.status = :status order by o.sequence asc", nativeQuery = true)
	List<OptionEntity> findByQuestionAndStatusOrderBySequenceAscNative(@Param("questionId") Long questionId, @Param("status") String status);
	
	Optional<OptionEntity> findByIdAndStatus(Long id, Status status);
}
