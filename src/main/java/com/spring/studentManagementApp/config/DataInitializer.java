package com.spring.studentManagementApp.config;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spring.studentManagementApp.dao.UsersDAO;
import com.spring.studentManagementApp.model.Users;

@Configuration
public class DataInitializer {
	@Autowired
	private Users users;

	@Bean
	CommandLineRunner loadSampleData(UsersDAO usersDAO,PasswordEncoder passwordEncoder) {
		return args -> {
			if(!usersDAO.existsByUsername("Admin")) {
				users.setUsername("Admin");
				users.setPassword(passwordEncoder.encode("admin@123"));
				users.setActive(true);
				usersDAO.save(users);				
			}
		};
	}
}
