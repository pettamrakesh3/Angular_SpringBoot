package com.example.CarRental.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.CarRental.entity.User;
import com.example.CarRental.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

	Optional<User> findFirstByEmail(String email);

	User findByUserRole(UserRole admin);

}
