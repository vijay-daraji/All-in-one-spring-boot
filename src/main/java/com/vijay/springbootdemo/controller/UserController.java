package com.vijay.springbootdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vijay.springbootdemo.dto.UserResponse;
import com.vijay.springbootdemo.entity.UserEntity;
import com.vijay.springbootdemo.service.UserService;

import jakarta.validation.Valid;


@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/welcome")
	public String getMessage() {
		return "Welcome to home page";
	}
	
	@GetMapping("/admin/welcome")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String welcomeAdmin() {
		return "Welcome Admin";
	}
	
	@GetMapping("/user/welcome")
	@PreAuthorize("hasAuthority('USER')")
	public String welcomeUser() {
		return "Welcome User";
	}
	
	@PostMapping("/users")
	public UserResponse createUser(@Valid @RequestBody UserEntity user) {
		return userService.createUser(user);
	}
	
	@GetMapping("/users/{email}")
	public UserResponse getUserByEmail(@PathVariable String email) {
		return userService.getUserByEmail(email);
	}
	
	@GetMapping("/users")
	public List<UserResponse> getUsers(){
		return userService.getUsers();
	}
	
	@PutMapping("/users/{email}")
	public UserResponse updateUser(@PathVariable String email, @Valid @RequestBody UserEntity userEntity) {
		return userService.updateUser(email,userEntity);
	}
	
	@DeleteMapping("/users/{email}")
	public String deleteUser(@PathVariable String email) {
		return userService.deleteUser(email);
	}
	
	@PostMapping("/users/auth/authenticate")
	public String authenticateAndGetToken(@RequestBody UserEntity user) {
		return userService.authenticateAndGetToken(user);
	}
}
