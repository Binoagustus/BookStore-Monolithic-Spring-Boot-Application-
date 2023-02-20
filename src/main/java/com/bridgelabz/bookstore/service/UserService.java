package com.bridgelabz.bookstore.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.bookstore.DTO.LoginDTO;
import com.bridgelabz.bookstore.exceptions.BookStoreException;
import com.bridgelabz.bookstore.model.UserModel;
import com.bridgelabz.bookstore.repository.UserRepository;
import com.bridgelabz.bookstore.utility.JWTUtils;

@Service
public class UserService implements IUserService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	JWTUtils jwtUtils;

	@Override
	public UserModel registerUser(UserModel user) {
		if (userRepo.findByEmail(user.getEmail()).isPresent()) {
			throw new BookStoreException("Mail Id already exists");
		} else {
			return userRepo.save(user);
		}
	}

	@Override
	public void deleteUserByToken(String token) {
		String email = jwtUtils.getEmailFromToken(token);
		Optional<UserModel> user = userRepo.findByEmail(email);
		userRepo.delete(user.get());
	}

	@Override
	public String userLogin(LoginDTO login) {
		String email = login.getEmail();
		String password = login.getPassword();
		Optional<UserModel> user = userRepo.findByEmailAndPassword(email, password);
		Optional<UserModel> userByEmail = userRepo.findByEmail(email);

		if (userByEmail.isPresent()) {
			if (user.isPresent()) {
				String token = jwtUtils.generateToken(login);
				user.get().setLogin(true);
				userRepo.save(user.get());
				return token;
			} else
				throw new BookStoreException("Invalid password. Try Again");
		} else
			throw new BookStoreException("Invalid Email Id");
	}


	@Override
	public void userLogout(String token) {
		String email = jwtUtils.getEmailFromToken(token);
		Optional<UserModel> user = userRepo.findByEmail(email);
		if (user.get().isLogin()) {
			user.get().setLogin(false);
			userRepo.save(user.get());
		} else
			throw new BookStoreException("User Inactive");
	}
}
