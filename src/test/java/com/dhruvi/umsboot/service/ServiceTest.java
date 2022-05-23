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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;

import com.dhruvi.umsboot.bean.EmailMessageBean;

//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.willDoNothing;

import com.dhruvi.umsboot.bean.User;
import com.dhruvi.umsboot.dao.UserDao;
import com.dhruvi.umsboot.utiity.EmailUtility;
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
	
	@Mock
	private EmailUtility emailUtility;
	
	@Mock
	private EmailMessageBean emailBean;
	
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

	@Test
	void userExistTest() {
		List<User> users = new ArrayList<User>();
		when(dao.findDistinctByEmail("test2@gmail.com")).thenReturn(users);
		
		assertEquals(service.userExist("test2@gmail.com"), "true");
		
		verify(dao, atLeast(1)).findDistinctByEmail(anyString());
		
	}
	
	@Test
	void userExistElseTest() {
		List<User> users = new ArrayList<User>();
		users.add(user);
		when(dao.findDistinctByEmail("test2@gmail.com")).thenReturn(users);
		
		assertEquals(service.userExist("test2@gmail.com"), "false");
		
		verify(dao, atLeast(1)).findDistinctByEmail(anyString());
		
	}

	
	@Test
	void getUserTest() throws Exception {
		
		List<User> userList = new ArrayList<User>();
		userList.add(user);
		when(dao.findByEmailAndPassword("test@gmail.com", "E1y/AM4//ss+dYKQ0kr5lQ==")).thenReturn(userList);
		
		User expectedUser = service.getUser("test@gmail.com","Dhruvi@123" );
		
		assertNotNull(expectedUser);
		
		verify(dao, atLeast(1)).findByEmailAndPassword(anyString(), anyString());
	
	}
	
	@Test
	void getUserWhenNotRegisteredTest() throws Exception {
		
		List<User> userList = new ArrayList<User>();
		when(dao.findByEmailAndPassword("test@gmail.com", "E1y/AM4//ss+dYKQ0kr5lQ==")).thenReturn(userList);
		User expectedUser = service.getUser("test@gmail.com","Dhruvi@123" );
		
		assertNull(expectedUser);
		
		verify(dao, atLeast(1)).findByEmailAndPassword(anyString(), anyString());
	
	}
	
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
	
	
	
	@Test
	void authenticateUserForForgetPassTest() {
		user.setUserid(1);
		user.setSecurity_question("book");
		user.setSecurity_answer("java");
		List<User> users = new ArrayList<User>();
		users.add(user);
		
		when(dao.findDistinctByEmail("test@gmail.com")).thenReturn(users);
		
		String msg = service.authenticateUserForForgetPass("test@gmail.com", "book", "java");
		
		assertEquals(msg, null);
		
		verify(dao, atLeast(1)).findDistinctByEmail(anyString());
	}
	
	@Test
	void authenticateUserForForgetPassTest1() {
		user.setUserid(1);
		user.setSecurity_question("book");
		user.setSecurity_answer("python");
		List<User> users = new ArrayList<User>();
		users.add(user);
		
		when(dao.findDistinctByEmail("test@gmail.com")).thenReturn(users);
		
		String msg = service.authenticateUserForForgetPass("test@gmail.com", "book", "java");
		
		assertEquals(msg, "Wrong security question or answer");
		
		verify(dao, atLeast(1)).findDistinctByEmail(anyString());
	}
	
	@Test
	void authenticateUserForForgetPassTest2() {
		user.setUserid(1);
		user.setSecurity_question("nick_name");
		user.setSecurity_answer("python");
		List<User> users = new ArrayList<User>();
		users.add(user);
		
		when(dao.findDistinctByEmail("test@gmail.com")).thenReturn(users);
		
		String msg = service.authenticateUserForForgetPass("test@gmail.com", "book", "java");
		
		assertEquals(msg, "Wrong security question or answer");
		
		verify(dao, atLeast(1)).findDistinctByEmail(anyString());
	}
	
	@Test
	void authenticateUserForForgetPassTest3() {
		user.setUserid(1);
		user.setSecurity_question("nick_name");
		user.setSecurity_answer("java");
		List<User> users = new ArrayList<User>();
		users.add(user);
		
		when(dao.findDistinctByEmail("test@gmail.com")).thenReturn(users);
		
		String msg = service.authenticateUserForForgetPass("test@gmail.com", "book", "java");
		
		assertEquals(msg, "Wrong security question or answer");
		
		verify(dao, atLeast(1)).findDistinctByEmail(anyString());
	}
	
	@Test
	void authenticateUserForgetPassWhenNotRegistered() {
		List<User> users = new ArrayList<User>();
		when(dao.findDistinctByEmail("test@gmail.com")).thenReturn(users);
		
		String msg = service.authenticateUserForForgetPass("test@gmail.com", "book", "java");
		assertEquals(msg, "Email is not registered");
		
		verify(dao, atLeast(1)).findDistinctByEmail(anyString());
	}
	
	@Test
	void authenticateUserForgetPassWhenWrongDetails() {
		user.setSecurity_question("nick_name");
		user.setSecurity_answer("ferry");
		
		List<User> users = new ArrayList<User>();
		users.add(user);
		when(dao.findDistinctByEmail("test@gmail.com")).thenReturn(users);
		
		String msg = service.authenticateUserForForgetPass("test@gmail.com", "book", "java");
		assertEquals(msg, "Wrong security question or answer");
		
		verify(dao, atLeast(1)).findDistinctByEmail(anyString());
	}
	

	@Test
	void sendOTPMailTest() throws MessagingException {
		
		String otp = service.sendOTPMail("test@gmail.com");
		
		assertNotNull(otp);
		
	}
	
	@Test
	void saveOtpTest() {
		
		user.setUserid(1);
		List<User> users = new ArrayList<User>();
		users.add(user);
		
		when(dao.findDistinctByEmail("test@gmail.com")).thenReturn(users);
		when(dao.save(user)).thenReturn(user);
		
		List<User> userList = dao.findDistinctByEmail("test@gmail.com");
		User expectedUser = userList.get(0);
		expectedUser.setOtp("202020");
		
		service.saveOTP("test@gmail.com", "202020");
		
		assertEquals(expectedUser.getOtp(), "202020");
		
		verify(dao, atLeast(1)).findDistinctByEmail(anyString());
		verify(dao,atLeast(1)).save(user);
		
	}
	
	@Test
	void updateUserTest() throws Exception {

		ps = new PasswordSecurity();

		user.setPassword(ps.encrypt("Dhruvi@123"));
		when(dao.save(user)).thenReturn(user);
		
		service.updateUser(user);
		verify(dao, atLeast(1)).save(any());
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
