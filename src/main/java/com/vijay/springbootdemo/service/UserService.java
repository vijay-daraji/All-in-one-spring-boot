package com.vijay.springbootdemo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vijay.springbootdemo.dto.UserResponse;
import com.vijay.springbootdemo.entity.UserEntity;
import com.vijay.springbootdemo.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	public UserResponse createUser(UserEntity user) {
		Optional<UserEntity> userOptional = userRepository.findByEmail(user.getEmail());
		if(userOptional.isPresent()) {
			throw new RuntimeException("User Already exist with email : "+user.getEmail());
		}
		UserEntity userEntity = userRepository.save(user);
		UserResponse userResponse = new UserResponse(userEntity.getId(),userEntity.getEmail(),userEntity.getRole());
		return userResponse;
	}

	public UserResponse getUserByEmail(String email) {
		Optional<UserEntity> userOptinal = userRepository.findByEmail(email);
		if(userOptinal.isEmpty()) {
			throw new RuntimeException("User not found with email : "+email);
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
			throw new RuntimeException("User not found with email : "+email);
		}
		UserEntity user = userOptional.get();
		user.setEmail(userEntity.getEmail());
		user.setPassword(userEntity.getPassword());
		user.setRole(userEntity.getRole());
		
		UserEntity updatedUser = userRepository.save(user);
		UserResponse userResponse = new UserResponse(updatedUser.getId(),updatedUser.getEmail(),updatedUser.getRole());
		return userResponse;
	}

	public String deleteUser(String email) {
		Optional<UserEntity> userOptinal = userRepository.findByEmail(email);
		if(userOptinal.isEmpty()) {
			throw new RuntimeException("User not found with email : "+email);
		}
		userRepository.deleteById(userOptinal.get().getId());
		return "User deleted";
	}

}
