package com.dhruvi.umsboot.controller;

import java.util.Base64;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dhruvi.umsboot.bean.Address;
import com.dhruvi.umsboot.bean.User;
import com.dhruvi.umsboot.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Controller
public class CustomController {

	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final String ADMINDASHBOARD = "adminDashboard";
	private static final String PROFILE = "profile";
	private static final String LOGIN = "login";
	private static final String ROLE = "role";
	private static final String ADMIN = "admin";
	private static final String USER = "user";
	private static final String REGISTRATION = "registration";
	private static final String PROFILEPICTURE = "profilepicture";
	private static final String USERID = "userid";
	private static final String SUCCESS_MSG = "success_msg";
	private static final String ERROR_MSG = "error_msg";

	private static final Logger logger = LogManager.getLogger(CustomController.class);

	@Autowired
	private UserService service;

	@RequestMapping(path = { "/", "/login" })
	public String home() {
		return LOGIN;
	}

	@RequestMapping("/registration")
	public String registration() {
		return REGISTRATION;
	}

	@RequestMapping("/adminDashboard")
	public String goToDashboard(HttpSession session) {

		String role = (String) session.getAttribute(ROLE);

		if (role != null && role.equals(USER)) {
			return PROFILE;
		}

		return ADMINDASHBOARD;
	}

	@RequestMapping("/profile")
	public String goToProfile(HttpSession session) {

		String role = (String) session.getAttribute(ROLE);

		if (role != null && role.equals(ADMIN)) {
			return ADMINDASHBOARD;
		}

		return PROFILE;
	}

	@RequestMapping("/forgetpassword")
	public String goForgetPassPage() {
		return "forgetpassword";
	}

	@RequestMapping("/fillOtp")
	public String showFillOtp() {
		return "fillOtp";
	}

	@PostMapping(path = "/process_form", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public String register(@Valid @ModelAttribute User user, BindingResult bindResult, @RequestParam(PROFILEPICTURE) MultipartFile filepart,
			Model model) {
		
		
		StringBuilder error = new StringBuilder();
		if (bindResult.hasErrors()) {
			List<FieldError> errors = bindResult.getFieldErrors();
			logger.info(errors);
			for (FieldError err : errors) {
				error.append(err.getDefaultMessage());
				model.addAttribute(ERROR_MSG, error);
				model.addAttribute(USER, user);
			}
			logger.info("builder = " + error);
			return REGISTRATION;
		} else {

			int id = 0;
			try {
				String base64Image = Base64.getEncoder().encodeToString(filepart.getBytes());

				user.setProfilepic(base64Image);
				id = service.create(user);

			} catch (Exception e) {
				logger.error(e);
			}

			if (id > 0) {
				model.addAttribute(SUCCESS_MSG, "User registered successfully");
				logger.info("User registered");
			} else {
				model.addAttribute(ERROR_MSG, "Error while registering user");

			}
		}
		return LOGIN;

	}

	@ResponseBody
	@RequestMapping("/checkEmailAvailability")
	public String checkEmail(@RequestParam String email) {

		String isExist = service.userExist(email);

		return isExist;
	}

	@RequestMapping("/process_login")
	public String checkLogin(@RequestParam(EMAIL) String email, @RequestParam(PASSWORD) String password,
			HttpServletRequest request, Model model) {
		User user = null;
		try {
			user = service.getUser(email, password);
		} catch (Exception e) {
			logger.error(e);
		}

		if (user == null) {

			model.addAttribute(ERROR_MSG, "Wrong Email or Password");
			model.addAttribute(EMAIL, email);
			return LOGIN;

		} else if (user.getRole().equals(USER)) {
			logger.info("user login");
			HttpSession session = request.getSession();
			session.setAttribute(ROLE, USER);
			session.setAttribute(USER, user);

			return "redirect:profile";
		} else {
			logger.info("admin login");
			HttpSession session = request.getSession();
			session.setAttribute(ROLE, ADMIN);

			return "redirect:adminDashboard";
		}
	}

	@ResponseBody
	@RequestMapping(path = "/getAllUsers", produces = "application/json")
	public JsonObject getAllUsers() {
		List<User> users = service.getUserList();

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

		JsonObject json = new JsonObject();
		json.add("data", gson.toJsonTree(users));
		logger.info(json);
		return json;

	}

	@ResponseBody
	@RequestMapping(path = "/deleteUserController")
	public String deleteuser(@RequestParam(USERID) int userid) {

		service.deleteUser(userid);
		logger.info("user deleted");
		return "true";
	}

	@PostMapping(path = "/editUserController")
	public String editUser(@RequestParam(USERID) String userid, HttpSession session) {

		session.setAttribute(USERID, userid);

		User user = null;
		try {
			user = service.getUserById(userid);
		} catch (Exception e) {
			logger.error(e);
		}

		session.setAttribute(USER, user);

		return REGISTRATION;
	}

	@RequestMapping("/logOutController")
	public String logout(HttpSession session) {

		logger.info("user logout");
		session.invalidate();
		return "redirect:login";
	}

	@ResponseBody
	@PostMapping(path = "/getUserAddress", produces = "application/json")
	public JsonElement getUserAddress(@RequestParam(USERID) String userid) {

		List<Address> address = null;
		try {
			address = service.getUserById(userid).getAddressList();
		} catch (Exception e) {
			logger.error(e);
		}
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJsonTree(address);

	}

	@PostMapping(path = "/process_edits", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public String editUserData(@ModelAttribute User editedUser, HttpSession session,
			@RequestParam(PROFILEPICTURE) MultipartFile filepart) {
		User user = (User) session.getAttribute(USER);

		editedUser.setEmail(user.getEmail());

		String base64Image = null;

		try {
			if (filepart.getSize() > 0) {
				base64Image = Base64.getEncoder().encodeToString(filepart.getBytes());

			} else {
				base64Image = user.getProfilepic();
			}

			editedUser.setProfilepic(base64Image);

			editedUser.getAddressList().stream().forEach(address -> {

				address.setUser(editedUser);

			});

			service.updateUser(editedUser);

		} catch (Exception e) {
			logger.error(e);
		}

		String role = (String) session.getAttribute(ROLE);
		logger.info("user edited successfully");
		if (role.equals(ADMIN)) {
			return "redirect:adminDashboard";
		} else {
			session.setAttribute(USER, editedUser);
			return "redirect:profile";
		}
	}

	@PostMapping("/forgetPasswordController")
	public String processForgetPassword(@RequestParam(EMAIL) String email,
			@RequestParam("security_question") String s_que, @RequestParam("security_answer") String s_ans,
			Model model) {

		String msg = service.authenticateUserForForgetPass(email, s_que, s_ans);

		if (msg == null) {
			model.addAttribute(EMAIL, email);
			String otp = null;
			try {
				otp = service.sendOTPMail(email);
			} catch (MessagingException e) {
				logger.error(e);
			}

			service.saveOTP(email, otp);

			return "fillOtp";
		} else {
			model.addAttribute(ERROR_MSG, msg);
			return "forgetpassword";
		}
	}

	@PostMapping("/verifyOTPController")
	public String checkOtp(@RequestParam(EMAIL) String email, @RequestParam("otp") String otp, Model model) {

		// verify otp

		model.addAttribute(EMAIL, email);
		boolean isCorrect = service.verifyOtp(email, otp);

		if (isCorrect) {
			return "resetpassword";

		} else {
			model.addAttribute(ERROR_MSG, "You have entered Wrong OTP");
			logger.info("Entered wrong otp");
			return "fillOtp";
		}
	}

	@PostMapping("/updatePasswordController")
	public String updatePassword(@RequestParam(PASSWORD) String password, @RequestParam(EMAIL) String email,
			Model model) {

		try {
			service.updatePssword(email, password);
		} catch (Exception e) {
			logger.error(e);
		}

		model.addAttribute(SUCCESS_MSG, "Password changed successfully");
		logger.info("Password changed sucessfully");
		return LOGIN;

	}
}