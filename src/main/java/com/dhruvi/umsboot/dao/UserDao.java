package com.dhruvi.umsboot.dao;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dhruvi.umsboot.bean.User;


public interface UserDao extends JpaRepository<User, Integer> {
		
	List<User> findDistinctByEmail(String email);
	
	List<User> findByEmailAndPassword(String email, String password);

	List<User> findByRole(String role);

}
