package com.vijay.springbootdemo.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.vijay.springbootdemo.entity.UserEntity;
import com.vijay.springbootdemo.exception.UserNotFoundException;
import com.vijay.springbootdemo.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> userOptional = userRepository.findByEmail(username);
		if(userOptional.isEmpty()) {
			throw new UserNotFoundException("user not found with username : "+username);
		}
		UserEntity userEntity = userOptional.get();
		CustomUserDetails userDetails = new CustomUserDetails(userEntity);
		return userDetails;
	}

}
