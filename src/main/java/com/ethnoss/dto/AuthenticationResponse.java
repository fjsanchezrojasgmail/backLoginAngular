package com.ethnoss.dto;

import com.ethnoss.enums.UserRole;

import lombok.Data;

@Data
public class AuthenticationResponse {

	private String jwt;
	private long userId;
	private UserRole userRole;
	
}
