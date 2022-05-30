package com.dhruvi.umsboot;

import java.util.Collection;
import java.util.Collections;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dhruvi.umsboot.bean.User;


public class UserPrincipal implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	private static Logger logger = Logger.getLogger(UserPrincipal.class);
	private static final Logger logger = LogManager.getLogger(UserPrincipal.class);
	
	private User user;
	
	

	public UserPrincipal(User user) {
		super();
		this.user = user;
		logger.info("----" + user);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		logger.info(user);
		if(user.getRole().equals("admin")) {
			logger.info("return admin");
			return Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}

		logger.info("return user" + user.getRole());
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
//		return Collections.singleton(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
		
	}
	
	public User getUser() {
		return this.user;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
