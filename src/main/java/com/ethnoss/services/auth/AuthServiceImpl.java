package com.ethnoss.services.auth;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ethnoss.dto.SignUpRequest;
import com.ethnoss.dto.UserDTO;
import com.ethnoss.entities.User;
import com.ethnoss.enums.UserRole;
import com.ethnoss.repositories.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	private final UserRepository userRepository;
	
	Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	
	@PostConstruct
	public void createAnAdminAccount() {
		Optional<User> optionalAdmin = userRepository.findByUserRole(UserRole.ADMIN);
		if(optionalAdmin.isEmpty()) {
			User admin = new User();
			admin.setName("Admin");
			admin.setEmail("admin@test.com");
			admin.setUserRole(UserRole.ADMIN);
			admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(admin);
			logger.info("Admin account created succesfully!");
			
		}else {
			logger.info("Admin account already exists!");
		}
	}

	@Override
	public Boolean hasUserWithEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findFirstByEmail(email).isPresent();
	}

	@Override
	public UserDTO signUp(SignUpRequest signUpRequest) {
		// TODO Auto-generated method stub
		User user = new User();
		user.setEmail(signUpRequest.getEmail());
		user.setName(signUpRequest.getName());
		user.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
		user.setUserRole(UserRole.CUSTOMER);
		return userRepository.save(user).getUserDTO();
	}

}
