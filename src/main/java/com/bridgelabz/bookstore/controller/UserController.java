package com.bridgelabz.bookstore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.bookstore.Response;
import com.bridgelabz.bookstore.DTO.LoginDTO;
import com.bridgelabz.bookstore.model.UserModel;
import com.bridgelabz.bookstore.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	IUserService service;

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@PostMapping("/registerUser")
	public ResponseEntity<Response> registerUser(@RequestBody UserModel user) {
		UserModel usermodel = service.registerUser(user);
		Response response = new Response("User Added Succesfully", usermodel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/* LOGIN API */
	@PutMapping("/login")
	public ResponseEntity<Response> loginByEmailAndPassword(@RequestBody LoginDTO login) {
		String fetchedUser = service.userLogin(login);
		Response response = new Response("User Logged in Succesfully", fetchedUser);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/* LOGOUT API */
	@GetMapping("/signout")
	public ResponseEntity<Response> userLogout(@RequestHeader String token) {
		service.userLogout(token);
		Response response = new Response("User Logged out Succesfully");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteUser")
	public ResponseEntity<Response> userDelete(@RequestHeader String token) {
		service.deleteUserByToken(token);
		Response response = new Response("User Deleted Succesfully");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
