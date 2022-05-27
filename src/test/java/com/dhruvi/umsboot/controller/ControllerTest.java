package com.dhruvi.umsboot.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import static org.mockito.Mockito.*;

import org.apache.tomcat.jni.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.dhruvi.umsboot.bean.Address;
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
			.andExpect(status().isOk())
			.andExpect(content().string("true"));
		
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
	
	@Test
	void processloginExceptionTest() throws Exception {
		
		when(service.getUser("test@gmail.com", "Dhruvi@123"))
			.thenThrow(new Exception());
		
		mockMvc.perform(get("/process_login")
				.param("email", "test@gmail.com")
				.param("password", "Dhruvi@123"))
			.andExpect(status().isOk());
			
		
		
		verify(service, atLeast(1)).getUser(anyString(), anyString());
	}
	
	@Test
	void getAllUsersTest() throws Exception {
		
		List<User> users = new ArrayList<User>();
		users.add(user);
		
		when(service.getUserList()).thenReturn(users);
		
		mockMvc.perform(get("/getAllUsers").contentType("application/json"))
		.andExpect(status().isOk());
		
		verify(service, atLeast(1)).getUserList();
		
		
	}
	
	@Test
	void deleteUserTest() throws Exception {
		
		doNothing().when(service).deleteUser(1);
		
		mockMvc.perform(get("/deleteUserController").param("userid", "1"))
			.andExpect(status().isOk())
			.andExpect(content().string("true"));
		
		verify(service, atLeast(1)).deleteUser(anyInt());
	}
	
	

	@Test
	void editUserTest() throws Exception {
		when(service.getUserById("1")).thenReturn(user);
		
		
		mockMvc.perform(post("/editUserController").param("userid", "1")
				.sessionAttr("userid", "1")
				.sessionAttr("user", user))
			.andExpect(status().isOk());
		
		
		verify(service, atLeast(1)).getUserById(anyString());
		
	}
	
	@Test
	void editUserExceptionTest() throws Exception {
		when(service.getUserById("1")).thenThrow(new Exception());
		
		
		mockMvc.perform(post("/editUserController").param("userid", "1")
				.sessionAttr("userid", "1")
				.sessionAttr("user", user))
			.andExpect(status().isOk());
		
		
		verify(service, atLeast(1)).getUserById(anyString());
		
	}
	
	
	@Test
	void logoutTest() throws Exception {
		
		mockMvc.perform(get("/logOutController"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("login"));
	}
	
	
	@Test
	void getUserAddressTest() throws Exception {
			List<Address> addressList = new ArrayList<Address>();
			Address address = new Address();
			
			address.setAddressid(2);
			address.setUser(user);
			address.setCity("Surat");
			addressList.add(address);
			user.setAddressList(addressList);
			
			
			when(service.getUserById("1")).thenReturn(user);
			
			mockMvc.perform(post("/getUserAddress").param("userid", "1"))
			.andExpect(status().isOk());
			
			verify(service, atLeast(1)).getUserById(anyString());
	}
	
	@Test
	void getUserAddressExceptionTest() throws Exception {
		
		when(service.getUserById("1")).thenThrow(new Exception());
		
		mockMvc.perform(post("/getUserAddress").param("userid", "1"))
		.andExpect(status().isOk());
		
		verify(service, atLeast(1)).getUserById(anyString());
	}
	
	@Test
	void updatePasswordTest() throws Exception {
		
		doNothing().when(service).updatePssword("test@gmail.com", "Dhruvi@123");
		
		mockMvc.perform(post("/updatePasswordController")
				.param("password", "Dhruvi@123")
				.param("email", "test@gmail.com"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("success_msg", "Password changed successfully"));
		
		verify(service, atLeast(1)).updatePssword(anyString(), anyString());
	}
	
	@Test
	void checkOtpTest() throws Exception {
		
		when(service.verifyOtp("test@gmail.com", "101010")).thenReturn(true);
		
		mockMvc.perform(post("/verifyOTPController")
				.param("email", "test@gmail.com")
				.param("otp", "101010"))
			.andExpect(status().isOk());
		
		verify(service, atLeast(1)).verifyOtp(anyString(), anyString());
		
	}
	
	@Test
	void checkOtpElseTest() throws Exception {
		
		when(service.verifyOtp("test@gmail.com", "101010")).thenReturn(false);
		
		mockMvc.perform(post("/verifyOTPController")
				.param("email", "test@gmail.com")
				.param("otp", "101010"))
			.andExpect(status().isOk());
	
		verify(service, atLeast(1)).verifyOtp(anyString(), anyString());
	}
	
	@Test
	void updatePasswordExceptionTest() throws Exception {
		
		
		doThrow(new Exception()).when(service).updatePssword("test@gmail.com", "Dhruvi@123");
		mockMvc.perform(post("/updatePasswordController")
				.param("password", "Dhruvi@123")
				.param("email", "test@gmail.com"))
			.andExpect(status().isOk());
		
		verify(service, atLeast(1)).updatePssword(anyString(), anyString());
		
	}
	
	@Test
	void processForgetPassElseTest() throws Exception {
		
		String msg = "Wrong security question or answer";
		
		when(service.authenticateUserForForgetPass("test@gmail.com","nick_name" , "ferry")).thenReturn(msg);
		
		mockMvc.perform(post("/forgetPasswordController")
				.param("email", "test@gmail.com")
				.param("security_question", "nick_name")
				.param("security_answer", "ferry"))
			.andExpect(status().isOk());
		
		verify(service, atLeast(1)).authenticateUserForForgetPass(anyString(), anyString(), anyString());
	}
	
	@Test
	void processForgetPassTest() throws Exception {
		
		String msg = null;
		
		when(service.authenticateUserForForgetPass("test@gmail.com","nick_name" , "ferry")).thenReturn(msg);
		when(service.sendOTPMail("test@gmail.com")).thenReturn("101010");
		doNothing().when(service).saveOTP("test@gmail.com", "101010");
		mockMvc.perform(post("/forgetPasswordController")
				.param("email", "test@gmail.com")
				.param("security_question", "nick_name")
				.param("security_answer", "ferry"))
			.andExpect(status().isOk());
		
		verify(service, atLeast(1)).saveOTP(anyString(),anyString());
		verify(service, atLeast(1)).sendOTPMail(anyString());
		verify(service, atLeast(1)).authenticateUserForForgetPass(anyString(), anyString(), anyString());
	}
	
	@Test
	void processForgetPassExceptionTest() throws Exception {
		
		String msg = null;
		
		when(service.authenticateUserForForgetPass("test@gmail.com","nick_name" , "ferry")).thenReturn(msg);
		when(service.sendOTPMail("test@gmail.com")).thenThrow(new MessagingException());

		mockMvc.perform(post("/forgetPasswordController")
				.param("email", "test@gmail.com")
				.param("security_question", "nick_name")
				.param("security_answer", "ferry"))
			.andExpect(status().isOk());
		
		
		verify(service, atLeast(1)).sendOTPMail(anyString());
		verify(service, atLeast(1)).authenticateUserForForgetPass(anyString(), anyString(), anyString());
	}
	
	
	@Test
	void processUserEditTest() throws IOException, Exception {

		
		User newUser = new User();
		
		newUser.setEmail("test@gmail.com");
		newUser.setUserid(1);
		newUser.setFirstname("Test");
		newUser.setLastname("Test");
		newUser.setBirthdate("1212-12-12");
		newUser.setDesignation("jrDeveloper");
		newUser.setGender("male");
		newUser.setRole("user");
		
		Address address = new Address();
		address.setAddressid(2);
		address.setCity("Surat");
		address.setCountry("India");
		address.setPostal_code("303030");
		address.setStreet_address("B-11");
		address.setState("Gujarat");
		address.setUser(newUser);
		List<Address> addressList = new ArrayList<Address>();
		addressList.add(address);
		
		newUser.setAddressList(addressList);
		
		doNothing().when(service).updateUser(newUser);
		
		FileInputStream file = new FileInputStream(
				"C:\\Users\\hp\\Pictures\\Screenshots\\download.jpg");
		MockMultipartFile profilepic = new MockMultipartFile("profilepicture", file);
		
		mockMvc.perform(MockMvcRequestBuilders.multipart("/process_edits")
				.file(profilepic)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.characterEncoding("UTF-8")
				.flashAttr("user", newUser)
				.sessionAttr("user", newUser)
				.sessionAttr("role", "user"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("profile"));
		
		verify(service, atLeast(1)).updateUser(any());
		
	}
	
	@Test
	void processAdminEditTest() throws IOException, Exception {

		
		User newUser = new User();
		
		newUser.setEmail("test@gmail.com");
		newUser.setUserid(1);
		newUser.setFirstname("Test");
		newUser.setLastname("Test");
		newUser.setBirthdate("1212-12-12");
		newUser.setDesignation("jrDeveloper");
		newUser.setGender("male");
		newUser.setRole("user");
		
		Address address = new Address();
		address.setAddressid(2);
		address.setCity("Surat");
		address.setCountry("India");
		address.setPostal_code("303030");
		address.setStreet_address("B-11");
		address.setState("Gujarat");
		address.setUser(newUser);
		List<Address> addressList = new ArrayList<Address>();
		addressList.add(address);
		
		newUser.setAddressList(addressList);
		
		doNothing().when(service).updateUser(newUser);
		
		FileInputStream file = new FileInputStream(
				"C:\\Users\\hp\\Pictures\\Screenshots\\download.jpg");
		MockMultipartFile profilepic = new MockMultipartFile("profilepicture", file);
		
		mockMvc.perform(MockMvcRequestBuilders.multipart("/process_edits")
				.file(profilepic)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.characterEncoding("UTF-8")
				.flashAttr("user", newUser)
				.sessionAttr("user", newUser)
				.sessionAttr("role", "admin"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("adminDashboard"));
		
		verify(service, atLeast(1)).updateUser(any());
		
	}
	
//	@Test
//	void processWhenNoProfilePicEditTest() throws IOException, Exception {
//
//		
//		User newUser = new User();
//		
//		newUser.setEmail("test@gmail.com");
//		newUser.setUserid(1);
//		newUser.setFirstname("Test");
//		newUser.setLastname("Test");
//		newUser.setBirthdate("1212-12-12");
//		newUser.setDesignation("jrDeveloper");
//		newUser.setGender("male");
//		newUser.setRole("user");
//		
//		Address address = new Address();
//		address.setAddressid(2);
//		address.setCity("Surat");
//		address.setCountry("India");
//		address.setPostal_code("303030");
//		address.setStreet_address("B-11");
//		address.setState("Gujarat");
//		address.setUser(newUser);
//		List<Address> addressList = new ArrayList<Address>();
//		addressList.add(address);
//		
//		newUser.setAddressList(addressList);
//		
//		doNothing().when(service).updateUser(newUser);
//		
//		FileInputStream file = new FileInputStream(
//				"");
//		MockMultipartFile profilepic = new MockMultipartFile("profilepicture", file);
//		
//		mockMvc.perform(MockMvcRequestBuilders.multipart("/process_edits")
//				.file(profilepic)
//				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//				.characterEncoding("UTF-8")
//				.flashAttr("user", newUser)
//				.sessionAttr("user", newUser)
//				.sessionAttr("role", "admin"))
//			.andExpect(status().is3xxRedirection());
//		
//		verify(service, atLeast(1)).updateUser(any());
//		
//	}

	
	@Test
	void processExceptionEditTest() throws IOException, Exception {
		
		User newUser = new User();
		
		newUser.setEmail("test@gmail.com");
		newUser.setUserid(1);
		newUser.setFirstname("Test");
		newUser.setLastname("Test");
		newUser.setBirthdate("1212-12-12");
		newUser.setDesignation("jrDeveloper");
		newUser.setGender("male");
		newUser.setRole("user");
		
		Address address = new Address();
		address.setAddressid(2);
		address.setCity("Surat");
		address.setCountry("India");
		address.setPostal_code("303030");
		address.setStreet_address("B-11");
		address.setState("Gujarat");
		address.setUser(newUser);
		List<Address> addressList = new ArrayList<Address>();
		addressList.add(address);
		
		newUser.setAddressList(addressList);
		
		
		doThrow(new Exception()).when(service).updateUser(newUser);
		FileInputStream file = new FileInputStream(
				"C:\\Users\\hp\\Pictures\\Screenshots\\download.jpg");
		MockMultipartFile profilepic = new MockMultipartFile("profilepicture", file);
		
		mockMvc.perform(MockMvcRequestBuilders.multipart("/process_edits")
				.file(profilepic)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.characterEncoding("UTF-8")
				.flashAttr("user", newUser)
				.sessionAttr("user", newUser)
				.sessionAttr("role", "admin"))
			.andExpect(status().is3xxRedirection());
		
		verify(service, atLeast(1)).updateUser(any());
	}
	
}
