package com.ethnoss.dto;

import com.ethnoss.enums.UserRole;

import lombok.Data;

@Data
public class UserDTO {
	
	private long userId;
	
	private String name;
	
	private String email;
	
	private UserRole userRole;

}
