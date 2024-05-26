package com.ethnoss.repositories;

import com.ethnoss.entities.User;
import com.ethnoss.enums.UserRole;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	
	Optional<User> findFirstByEmail(String email);
	Optional<User> findByUserRole(UserRole role);

}
