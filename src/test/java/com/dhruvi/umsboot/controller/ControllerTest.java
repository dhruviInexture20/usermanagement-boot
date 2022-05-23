package com.dhruvi.umsboot.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.lang.ProcessBuilder.Redirect;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties.View;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.dhruvi.umsboot.bean.User;
import com.dhruvi.umsboot.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@InjectMocks
	CustomController controller;
	
	@MockBean
	private UserService service;
	
	
	private User user;
	
	@BeforeAll
	void setUp() {
		user = new User();
		user.setEmail("test@gmail.com");
		user.setUserid(1);
	}
	
	@Test
	void loginUrlTest() throws Exception {
		
		mockMvc.perform(get("/login"))
        .andExpect(status().isOk());
	}
	
	@Test
	void blankUrltest() throws Exception {
		mockMvc.perform(get("/"))
        .andExpect(status().isOk());
	}
	
	@Test
	void registrationPageUrlTest() throws Exception {
		mockMvc.perform(get("/registration"))
			.andExpect(status().isOk());
	}
	
	@Test
	void dashBoardUrlTest() throws Exception {
		mockMvc.perform(get("/adminDashboard")
				.sessionAttr("role", "admin"))
			.andExpect(status().isOk());
	}
	
	@Test
	void dashBoardUrlTest2() throws Exception {
		mockMvc.perform(get("/adminDashboard")
				.sessionAttr("role", "user"))
			.andExpect(status().isOk());
	}
	
//	@Test
//	void dashBoardUrlTest3() throws Exception {
//		mockMvc.perform(get("/adminDashboard").sessionAttr("role", null)).andExpect(status().isOk());
//	}
	
	// profile
	@Test
	void profileurlTest() throws Exception {
		mockMvc.perform(get("/profile")
				.sessionAttr("role", "user"))
			.andExpect(status().isOk());
	}
	
	@Test
	void profileUrlTest2() throws Exception {
		mockMvc.perform(get("/profile")
				.sessionAttr("role", "admin"))
			.andExpect(status().isOk());
	}
	
	
	@Test
	void forgetPassUrlTest() throws Exception {
		mockMvc.perform(get("/forgetpassword"))
		.andExpect(status().isOk());
	}
	
	@Test
	void fillOtpUrlTest() throws Exception {
		mockMvc.perform(get("/fillOtp"))
			.andExpect(status().isOk());
	}
	
	@Test
	void checkEmailAvailableTest() throws Exception {
		when(service.userExist("test@gmail.com")).thenReturn("true");
		mockMvc.perform(get("/checkEmailAvailability")
				.param("email", "test@gmail.com"))
			.andExpect(status().isOk());
		
		verify(service, atLeast(1)).userExist(anyString());
	}
	
	@Test
	void processUserLoginTest() throws Exception {
		user.setRole("user");
		when(service.getUser("test@gmail.com", "Dhruvi@123"))
			.thenReturn(user);
		
		mockMvc.perform(get("/process_login")
				.param("email", "test@gmail.com")
				.param("password", "Dhruvi@123"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("profile"));
		
		verify(service, atLeast(1)).getUser(anyString(), anyString());
	}
	
	@Test
	void processAdminLoginTest() throws Exception {
		user.setRole("admin");
		when(service.getUser("test@gmail.com", "Dhruvi@123"))
			.thenReturn(user);
		
		mockMvc.perform(get("/process_login")
				.param("email", "test@gmail.com")
				.param("password", "Dhruvi@123"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("adminDashboard"));
		
		verify(service, atLeast(1)).getUser(anyString(), anyString());
	}
	
	@Test
	void processNullUserLoginTest() throws Exception {
		User newUser = null;
		when(service.getUser("test@gmail.com", "Dhruvi@123"))
			.thenReturn(newUser);
		
		mockMvc.perform(get("/process_login")
				.param("email", "test@gmail.com")
				.param("password", "Dhruvi@123"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("error_msg", "Wrong Email or Password"))
			.andExpect(model().attribute("email", "test@gmail.com"));
		
		verify(service, atLeast(1)).getUser(anyString(), anyString());
	}
	
//	@Test
//	void processloginExceptionTest() throws Exception {
//		
//		when(service.getUser("test@gmail.com", "Dhruvi@123"))
//			.thenThrow(new Exception());
//		
//		mockMvc.perform(get("/process_login")
//				.param("email", "test@gmail.com")
//				.param("password", "Dhruvi@123"))
//			
//		
//		
//		verify(service, atLeast(1)).getUser(anyString(), anyString());
//	}
	
	
}
