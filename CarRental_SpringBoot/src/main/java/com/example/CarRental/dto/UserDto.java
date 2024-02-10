package com.example.CarRental.dto;

import com.example.CarRental.enums.UserRole;

import lombok.Data;

@Data
public class UserDto {
	private Long id;
	
	private String name;
	
	private String email;
	
	private UserRole userRole;
}
