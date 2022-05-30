package com.dhruvi.umsboot;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dhruvi.umsboot.bean.User;
import com.dhruvi.umsboot.dao.UserDao;


@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserDao dao;
	
	private static final Logger logger = LogManager.getLogger(CustomUserDetailsService.class);
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
//		User user =  dao.g();
		
		System.out.println("email = " + email);
		List<User> users = dao.findDistinctByEmail(email);
		
		User user = users.size()>0 ? users.get(0) : null;
		
		System.out.println("---------------------------------");
		System.out.println(user);
		logger.info("----------------");
		logger.info(user);
		if(user == null) {
			throw new UsernameNotFoundException("User Not Available");
		}
		
		return new UserPrincipal(user);
		
		
	}
	
}
