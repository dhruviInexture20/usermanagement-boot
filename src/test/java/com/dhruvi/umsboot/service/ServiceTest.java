package com.dhruvi.umsboot.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.AtLeast;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

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
	
	@Mock
	private ArrayList<User> mockArrayList;
	
	@BeforeAll
	void start() {	
		service = new UserServiceImpl();
	}
	
	@Test
	void createTest() throws Exception {
		User user = new User();
		
		when(ps.encrypt("Dhruvi@123")).thenReturn("E1y/AM4//ss+dYKQ0kr5lQ==");
		when(ps.decrypt("E1y/AM4//ss+dYKQ0kr5lQ==")).thenReturn("Dhruvi@123");
//		
		user.setFirstname("Test");
		user.setLastname("Patel");
		user.setEmail("test@gmail.com");
		user.setBirthdate("2020-12-12");
		user.setPassword(ps.encrypt("Dhruvi@123"));
		
//		User user = mock(User.class);
		
		User expectedUser = new User();
		expectedUser.setUserid(1);
		expectedUser.setEmail("test@gmail.com");
		expectedUser.setFirstname("Test");
		expectedUser.setLastname("Patel");
		expectedUser.setPassword(ps.decrypt("E1y/AM4//ss+dYKQ0kr5lQ=="));
		when(dao.save(user)).thenReturn(expectedUser);
		
		int userid = service.create(user);
		
		assertEquals(userid, 1);
		
		
		verify(dao, atLeast(1)).save(any());
	}
	
//	@Test
//	void createUserExceptionTest() throws Exception {
//		
//		//User user = new User();
//		
//		PasswordSecurity ps = new PasswordSecurity();
//
//		when(ps.encrypt("Dhruvi@123")).thenThrow(new Exception());
//	
////		user.setPassword(ps.encrypt("Dhruvi@123")); // encrypt
//	
//		assertThrows(Exception.class, () -> ps.encrypt("Dhruvi@123"));
//		
////		verify(dao, atLeast(1)).save(any());
//	}
//	

	@Test
	void getUserListTest() {
		
		dao.findByRole("user");
		
		when(dao.findByRole("user")).thenReturn(mockArrayList);
		
		List<User> actualList = service.getUserList();
		
		assertEquals(actualList, mockArrayList);
	}
	
	@Test
	void deleteUserTest() {
		
//		long employeeId = 1L;
//
//        willDoNothing().given(employeeRepository).deleteById(employeeId);
//
//        // when -  action or the behaviour that we are going test
//        employeeService.deleteEmployee(employeeId);
//
//        // then - verify the output
//        verify(employeeRepository, times(1)).deleteById(employeeId);
		
		int userid = 1;
		
		willDoNothing().given(dao).deleteById(userid);
		service.deleteUser(userid);
		
		
		
	}

}
