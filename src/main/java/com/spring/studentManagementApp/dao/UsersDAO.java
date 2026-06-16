package com.spring.studentManagementApp.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import com.spring.studentManagementApp.model.Users;

@Repository
public interface UsersDAO extends JpaRepository<Users,Long> {
	boolean existsByUsername(String username);
	Optional<Users> findByUsername(String username); 
}
