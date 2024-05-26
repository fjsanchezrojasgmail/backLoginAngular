package com.ethnoss.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.ethnoss.dto.AuthenticationRequest;
import com.ethnoss.dto.AuthenticationResponse;
import com.ethnoss.dto.SignUpRequest;
import com.ethnoss.dto.UserDTO;
import com.ethnoss.entities.User;
import com.ethnoss.repositories.UserRepository;
import com.ethnoss.services.auth.AuthService;
import com.ethnoss.services.jwt.UserService;
import com.ethnoss.utils.JWTUtil;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
	
	private final AuthService authService;
	
	private final UserService userService;
	
	private final JWTUtil jwtUtil;
	
	private final UserRepository userRepository;
	
	private final AuthenticationManager authenticationManager;
	
	
	@Operation(summary = "SignUp", description ="SignUp operation")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
	        @ApiResponse(responseCode = "404", description = "User not found", content = 
	            { @Content(mediaType = "application/json", schema = 
	            @Schema(implementation = User.class ) ) } ) } )
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest){
		if(authService.hasUserWithEmail(signUpRequest.getEmail())) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exists!.");
		}
		
		UserDTO userDTO = authService.signUp(signUpRequest);
		
		if(userDTO == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
		
	}
	
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest) {
		try {
			
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
						authenticationRequest.getEmail(),authenticationRequest.getPassword()));
			
		}catch(BadCredentialsException e) {
			throw new BadCredentialsException("Invalid user or password!.");
		}
		
		final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
		
		Optional<User> optionalUser = userRepository.findFirstByEmail(authenticationRequest.getEmail());
		
		final String jwt = jwtUtil.generateToken(userDetails);
		
		AuthenticationResponse response = new AuthenticationResponse();
		
		if(optionalUser.isPresent()) {
			response.setJwt(jwt);
			response.setUserRole(optionalUser.get().getUserRole());
		    response.setUserId(optionalUser.get().getUserId());
		}
		
		return response;
		

		
	}
	

}
