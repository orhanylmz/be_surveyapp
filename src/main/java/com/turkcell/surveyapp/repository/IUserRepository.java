package com.turkcell.surveyapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.surveyapp.entity.UserEntity;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, String> {
	Optional<UserEntity> findByUsernameAndPassword(String username, String password);
}
