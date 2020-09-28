package com.turkcell.surveyapp.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.turkcell.surveyapp.util.Utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {
	@CreatedDate
	@Column(name = "created_at")
	LocalDateTime createdAt;

	@Column(name = "created_by")
	String createdBy;

	@LastModifiedDate
	@Column(name = "updated_at")
	LocalDateTime updatedAt;

	@Column(name = "updated_by")
	String updatedBy;

	@PrePersist
	void prePersist() {
		createdAt = updatedAt = Utils.now();
		updatedBy = createdBy;
	}

	@PreUpdate
	void preUpdate() {
		updatedAt = Utils.now();
	}
}
