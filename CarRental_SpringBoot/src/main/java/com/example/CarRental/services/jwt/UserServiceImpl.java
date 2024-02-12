package com.example.CarRental.services.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.CarRental.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        logger.debug("Creating UserDetailsService");
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                logger.debug("Loading user details for username: {}", username);
                UserDetails userDetails = userRepository.findFirstByEmail(username)
                        .orElseThrow(() -> {
                            logger.error("User not found for username: {}", username);
                            return new UsernameNotFoundException("User not found");
                        });
                logger.debug("User details loaded successfully for username: {}", username);
                return userDetails;
            }
        };
    }
}

