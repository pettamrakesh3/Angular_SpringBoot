package com.example.CarRental.services.jwt;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.CarRental.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl  implements UserService{

	private final UserRepository userRepository;
	
	@Override
	public UserDetailsService userDetailsService() {
		// TODO Auto-generated method stub
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) {
				// TODO Auto-generated method stub
				return userRepository.findFirstByEmail(username).orElseThrow(
						
						() -> new UsernameNotFoundException("user not found")
						);
			}
			
		};
	}

}
