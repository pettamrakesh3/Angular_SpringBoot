package com.example.CarRental.services.auth;

import com.example.CarRental.dto.SignUpRequest;
import com.example.CarRental.dto.UserDto;

public interface AuthService {
		
	UserDto createCustomer(SignUpRequest signUpRequest);
	
	boolean hasCustomerWithEmail(String email);
}
