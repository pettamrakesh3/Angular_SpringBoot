package com.example.CarRental.services.auth;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.CarRental.dto.SignUpRequest;
import com.example.CarRental.dto.UserDto;
import com.example.CarRental.entity.User;
import com.example.CarRental.enums.UserRole;
import com.example.CarRental.repository.UserRepository;
import com.example.CarRental.services.auth.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	
	
	private final UserRepository userRepository;
	
	private final ModelMapper modelMapper;

	@Override
	public UserDto createCustomer(SignUpRequest signUpRequest) {
		// TODO Auto-generated method stub
		
		signUpRequest.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
		User user=modelMapper.map(signUpRequest, User.class);
		user.setUserRole(UserRole.CUSTOMER);
		User createdUser=userRepository.save(user);
		UserDto userDto=modelMapper.map(createdUser, UserDto.class);
		return userDto;
	}

	@Override
	public boolean hasCustomerWithEmail(String email) {
		// TODO Auto-generated method stub	
		
		return userRepository.findFirstByEmail(email).isPresent();
	}

}
