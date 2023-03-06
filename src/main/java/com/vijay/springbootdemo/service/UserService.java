package com.vijay.springbootdemo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vijay.springbootdemo.dto.UserResponse;
import com.vijay.springbootdemo.entity.UserEntity;
import com.vijay.springbootdemo.exception.UserAlreadyExistException;
import com.vijay.springbootdemo.exception.UserNotFoundException;
import com.vijay.springbootdemo.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService; 
	
	@Autowired
	private AuthenticationManager authenticationManager; 

	public UserResponse createUser(UserEntity user) {
		Optional<UserEntity> userOptional = userRepository.findByEmail(user.getEmail());
		if(userOptional.isPresent()) {
			throw new UserAlreadyExistException("User Already exist with email : "+user.getEmail());
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		UserEntity userEntity = userRepository.save(user);
		UserResponse userResponse = new UserResponse(userEntity.getId(),userEntity.getEmail(),userEntity.getRole());
		return userResponse;
	}

	public UserResponse getUserByEmail(String email) {
		Optional<UserEntity> userOptinal = userRepository.findByEmail(email);
		if(userOptinal.isEmpty()) {
			throw new UserNotFoundException("User not found with email : "+email);
		}
		UserEntity userEntity = userOptinal.get();
		UserResponse userResponse = new UserResponse(userEntity.getId(),userEntity.getEmail(),userEntity.getRole());
		return userResponse;
	}

	public List<UserResponse> getUsers() {
		List<UserEntity> list = userRepository.findAll();
		List<UserResponse> response = list.stream().map(userEntity -> 
			new UserResponse(userEntity.getId(),userEntity.getEmail(),userEntity.getRole()))
				.collect(Collectors.toList());
		return response;
	}

	public UserResponse updateUser(String email, UserEntity userEntity) {
		Optional<UserEntity> userOptional = userRepository.findByEmail(email);
		if(userOptional.isEmpty()) {
			throw new UserNotFoundException("User not found with email : "+email);
		}
		UserEntity user = userOptional.get();
		user.setEmail(userEntity.getEmail());
		user.setPassword(passwordEncoder.encode(userEntity.getPassword()));
		user.setRole(userEntity.getRole());
		
		UserEntity updatedUser = userRepository.save(user);
		UserResponse userResponse = new UserResponse(updatedUser.getId(),updatedUser.getEmail(),updatedUser.getRole());
		return userResponse;
	}

	public String deleteUser(String email) {
		Optional<UserEntity> userOptinal = userRepository.findByEmail(email);
		if(userOptinal.isEmpty()) {
			throw new UserNotFoundException("User not found with email : "+email);
		}
		userRepository.deleteById(userOptinal.get().getId());
		return "User deleted";
	}

	public String authenticateAndGetToken(UserEntity user) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
		if(authenticate.isAuthenticated()) {
			return jwtService.generateToken(user.getEmail());
		}
		else {
			throw new UsernameNotFoundException("Invalid user request!!");
		}
	}

}
