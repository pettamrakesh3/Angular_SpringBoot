package com.example.CarRental.controller;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final UserService userService;
	private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signupCustomer(@RequestBody SignUpRequest signupRequest) {
        try {
            logger.info("API called successfully for sign up.");
            if (authService.hasCustomerWithEmail(signupRequest.getEmail())) {
                return new ResponseEntity<>("Customer already exists with the given email", HttpStatus.NOT_ACCEPTABLE);
            }

            UserDto createdCustomerDto = authService.createCustomer(signupRequest);
            if (createdCustomerDto == null) {
                return new ResponseEntity<>("Customer not created. Please try again.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(createdCustomerDto, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error occurred during customer registration", e);
            return new ResponseEntity<>("Customer not registered. An error occurred.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws BadCredentialsException, DisabledException, UsernameNotFoundException {
        try {
            logger.info("API called successfully for login.");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            logger.error("Incorrect username or password", e);
            throw new BadCredentialsException("Incorrect username or password");
        }

        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());

        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());

        final String jwt = jwtUtils.generateToken(userDetails);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        if (optionalUser.isPresent()) {
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authenticationResponse;
    }
}
