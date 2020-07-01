package com.psl.app.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.psl.app.model.User;
import com.psl.app.repo.UserRepository;

@Service
public class UserService implements UserDetailsService{

	@Autowired
	UserRepository dao;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=dao.findByUsername(username);
		
		if(user==null)
		{
			throw new UsernameNotFoundException("Invalid username or password");
		}
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
	
		
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),authorities);
	}
	
	

}
