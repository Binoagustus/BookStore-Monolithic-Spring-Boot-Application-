package com.bridgelabz.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.bookstore.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer>{

	Optional<UserModel> findByEmail(String email);
	
	Optional<UserModel> findByEmailAndPassword(String email, String password);
}
