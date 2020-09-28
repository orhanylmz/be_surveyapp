package com.turkcell.surveyapp.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "survey_user_option", uniqueConstraints = { @UniqueConstraint(columnNames = { "fk_username", "fk_question_id" }) })
public class UserOptionEntity extends BaseEntity {
	@Id
	@SequenceGenerator(name = "survey_user_option_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "survey_user_option_id_seq")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_username")
	private UserEntity user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_survey_id")
	private SurveyEntity survey;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_question_id")
	private QuestionEntity question;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_option_id")
	private OptionEntity option;
}
