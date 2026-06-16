package com.spring.studentManagementApp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.studentManagementApp.dao.UsersDAO;
import com.spring.studentManagementApp.model.Users;

@Service
public class UserServiceImpl implements UserDetailsService {

	@Autowired
	private UsersDAO userDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users users = userDAO.findByUsername(username)
		.orElseThrow(() -> new UsernameNotFoundException("Invalid username"));
		
		return User.withUsername(username)
				.password(users.getPassword())
				.disabled(!users.isActive())
				.build();
	}

}
