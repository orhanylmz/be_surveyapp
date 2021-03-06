package com.turkcell.surveyapp.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.turkcell.surveyapp.enumeration.Status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "survey_option", uniqueConstraints = { @UniqueConstraint(columnNames = { "fk_question_id", "sequence" }) })
public class OptionEntity extends BaseEntity {
	@Id
	@SequenceGenerator(name = "survey_option_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "survey_option_id_seq")
	private Long id;

	private String option;

	private Integer sequence;
	
	@Enumerated(EnumType.STRING)
	private Status status = Status.PASSIVE;

	private Integer responseCount = 0;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_question_id")
	private QuestionEntity question;
}
