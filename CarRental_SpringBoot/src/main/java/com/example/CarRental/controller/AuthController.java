package com.example.CarRental.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CarRental.dto.AuthenticationRequest;
import com.example.CarRental.dto.AuthenticationResponse;
import com.example.CarRental.dto.SignUpRequest;
import com.example.CarRental.dto.UserDto;
import com.example.CarRental.entity.User;
import com.example.CarRental.repository.UserRepository;
import com.example.CarRental.services.auth.AuthService;
import com.example.CarRental.services.jwt.UserService;
import com.example.CarRental.utils.JWTUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	private final AuthenticationManager authenticationManager;
	
	private final UserService userService;
	
	private final JWTUtils jwtUtils;
	
	private final UserRepository userRepository;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signupCustomer(@RequestBody SignUpRequest signupRequest){
		
		try {
			
			System.out.println("api called success fully");
			if(authService.hasCustomerWithEmail(signupRequest.getEmail())) {
				return new ResponseEntity<>("Customer already exist with given mail",HttpStatus.NOT_ACCEPTABLE);
			}
			
			UserDto createdCustomerDto=authService.createCustomer(signupRequest);
			if(createdCustomerDto==null) {
				return new ResponseEntity<>("Customer not Created create again",HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(createdCustomerDto,HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>("Customer not registered",HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@PostMapping("/login")
	public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws
			BadCredentialsException,
			DisabledException,
			UsernameNotFoundException{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));
		}catch(BadCredentialsException e) {
			throw new BadCredentialsException("INcorrect username or password");
		}
		final UserDetails userDetails=userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
		
		Optional<User> optionalUser=userRepository.findFirstByEmail(userDetails.getUsername());
		
		final String jwt=jwtUtils.generateToken(userDetails);
		
		AuthenticationResponse authenticationResponse=new AuthenticationResponse();
		
		if(optionalUser.isPresent()) {
			authenticationResponse.setJwt(jwt);
			authenticationResponse.setUserId(optionalUser.get().getId());
			authenticationResponse.setUserRole(optionalUser.get().getUserRole());
		}
		return authenticationResponse;
	}
	
}
