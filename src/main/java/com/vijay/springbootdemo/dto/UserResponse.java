package com.vijay.springbootdemo.dto;

public class UserResponse {
	
	private int id;
	private String email;
	private String role;
	
	public UserResponse() {
		super();
	}
	public UserResponse(int id, String email, String role) {
		super();
		this.id = id;
		this.email = email;
		this.role = role;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	

}

