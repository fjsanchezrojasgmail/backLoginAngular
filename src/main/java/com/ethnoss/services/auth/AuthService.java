package com.ethnoss.services.auth;

import com.ethnoss.dto.SignUpRequest;
import com.ethnoss.dto.UserDTO;

public interface AuthService {
	
	UserDTO signUp(SignUpRequest signUpRequest);
	
	Boolean hasUserWithEmail(String email);
	
}
