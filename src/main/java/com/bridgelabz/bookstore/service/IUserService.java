package com.bridgelabz.bookstore.service;

import com.bridgelabz.bookstore.DTO.LoginDTO;
import com.bridgelabz.bookstore.model.UserModel;

public interface IUserService {

	UserModel registerUser(UserModel user);

	void deleteUserByToken(String token);

	String userLogin(LoginDTO login);

	void userLogout(String token);

}
