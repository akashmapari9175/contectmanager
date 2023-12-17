package com.contactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.contactmanager.entity.User;
import com.contactmanager.repository.UserRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		//fetching user from database
		User user = userRepository.getUserByUserName(email);
		
		if(user==null) {
			throw new UsernameNotFoundException("could not found user here !!");
		}
	//	CustomeUserDetails customeUserDetails = new CustomeUserDetails(user);
		
		return new CustomeUserDetails(user);
	}

}
