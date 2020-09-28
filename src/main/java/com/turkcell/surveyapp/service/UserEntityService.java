package com.turkcell.surveyapp.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.turkcell.surveyapp.entity.UserEntity;
import com.turkcell.surveyapp.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserEntityService {
	private final IUserRepository repository;

	public UserEntity save(UserEntity entity) {
		return repository.save(entity);
	}

	public Optional<UserEntity> findByUsernameAndPassword(String username, String password) {
		return repository.findByUsernameAndPassword(username, password);
	}

	public Page<UserEntity> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}
}
