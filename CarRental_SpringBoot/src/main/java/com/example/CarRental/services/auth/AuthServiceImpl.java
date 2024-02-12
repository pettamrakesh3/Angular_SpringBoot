package com.example.CarRental.services.auth;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto createCustomer(SignUpRequest signUpRequest) {
        logger.info("Creating customer with email: {}", signUpRequest.getEmail());
        signUpRequest.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
        User user = modelMapper.map(signUpRequest, User.class);
        user.setUserRole(UserRole.CUSTOMER);
        User createdUser = userRepository.save(user);
        UserDto userDto = modelMapper.map(createdUser, UserDto.class);
        logger.info("Customer created successfully with email: {}", userDto.getEmail());
        return userDto;
    }

    @Override
    public boolean hasCustomerWithEmail(String email) {
        logger.debug("Checking if customer exists with email: {}", email);
        boolean exists = userRepository.findFirstByEmail(email).isPresent();
        logger.debug("Customer with email {} exists: {}", email, exists);
        return exists;
    }
}
