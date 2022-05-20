package com.dhruvi.umsboot.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;

//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.willDoNothing;

import com.dhruvi.umsboot.bean.User;
import com.dhruvi.umsboot.dao.UserDao;
import com.dhruvi.umsboot.utiity.PasswordSecurity;


@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ServiceTest {
	
	@InjectMocks
	private UserService service;
	
	@Mock
	private UserDao dao;
	
	@Mock
	private PasswordSecurity ps;
	
	private User user;
	
	@BeforeAll
	void start() {	
		
		service = new UserServiceImpl();
		user = new User();
		user.setFirstname("Test");
		user.setLastname("Patel");
		user.setEmail("test@gmail.com");
	}
	
	@Test
	void createTest() throws Exception {
		
		user.setPassword("E1y/AM4//ss+dYKQ0kr5lQ==");
		
		User expectedUser = new User();
		expectedUser.setUserid(1);
		expectedUser.setEmail("test@gmail.com");
		expectedUser.setFirstname("Test");
		expectedUser.setLastname("Patel");
		expectedUser.setPassword("E1y/AM4//ss+dYKQ0kr5lQ==");
		when(dao.save(user)).thenReturn(expectedUser);
		
		int userid = service.create(user);
		
		assertEquals(userid, 1);
		
		
		verify(dao, atLeast(1)).save(any());
	}

	// userExist
	// getUser
	
	@Test
	void getUserListTest() {
		
		List<User> userList = new ArrayList<User>();
		user.setUserid(1);
		userList.add(user);
		
		when(dao.findByRole("user")).thenReturn(userList);
		
		List<User> actualList = service.getUserList();
		assertEquals(actualList.size(), 1);
		
		verify(dao, atLeast(1)).findByRole(anyString());
	}
	
	@Test
	void deleteUserTest() {
		
		int userid = 1;
		
		doNothing().when(dao).deleteById(userid);
		service.deleteUser(userid);
		
		verify(dao, atLeast(1)).deleteById(anyInt());
		
	}
	
	@Test
	void getUserByIdTest() throws Exception {
		user.setPassword("E1y/AM4//ss+dYKQ0kr5lQ==");
		user.setUserid(1);
		
		when(dao.getById(1)).thenReturn(user);
		User actualUser = service.getUserById("1");	
		
		assertThat(actualUser).isNotNull();
		assertThat(actualUser.getUserid()).isEqualTo(1);
		
		verify(dao, atLeast(1)).getById(anyInt());
		
	}
	
	
	
	// authenticateUserForForgetPass
	// sendOTPMail
	// saveOTP
	// updateUser
	
	@Test
	void updateUserTest() {
		
	}
	

	@Test
	void verifyOtpTest() {
		user.setOtp("202020");
		List<User> userList = new ArrayList<User>();
		userList.add(user);
		
		when(dao.findDistinctByEmail("test@gmail.com")).thenReturn( userList);
		
		assertTrue(service.verifyOtp("test@gmail.com", "202020"));
		
		assertFalse(service.verifyOtp("test@gmail.com", "121212"));
		
		verify(dao,atLeast(1)).findDistinctByEmail(anyString());
		
	}
	
	
	@Test
	void updatePasswordTest() throws Exception {
		
		List<User> userList = new ArrayList<User>();
		userList.add(user);
		
		when(dao.findDistinctByEmail("test@gmail.com")).thenReturn( userList);
		
		service.updatePssword("test@gmail.com", "Dhruvi@123");
		
		verify(dao,atLeast(1)).findDistinctByEmail(anyString());
		
	}
	

}
