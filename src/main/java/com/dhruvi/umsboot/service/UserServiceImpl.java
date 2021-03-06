package com.dhruvi.umsboot.service;



import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dhruvi.umsboot.bean.EmailMessageBean;
import com.dhruvi.umsboot.bean.User;
import com.dhruvi.umsboot.dao.UserDao;
import com.dhruvi.umsboot.utiity.DataUtility;
import com.dhruvi.umsboot.utiity.EmailUtility;
import com.dhruvi.umsboot.utiity.PasswordSecurity;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao dao;


	@Override
	public int create(User user) throws Exception {
		
//		PasswordSecurity ps = new PasswordSecurity();
//		String password = user.getPassword();
//	
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		encoder.encode(user.getPassword());
		user.setPassword(encoder.encode(user.getPassword())); // encrypt
		User savedUser = dao.save(user);
		
		return savedUser.getUserid();
	}

	@Override
	public String userExist(String email) {
	
		if (dao.findDistinctByEmail(email).isEmpty()) {
			return "true";
		} else
			return "false";

	}

	@Override
	public User getUser(String email, String password) throws Exception {
		User user = null;
//		PasswordSecurity ps = new PasswordSecurity();
//		String pass = ps.encrypt(password); // encrypt
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		List<User> users = dao.findByEmailAndPassword(email, encoder.encode(password));
		if(users.size() > 0) {
			user = users.get(0);
			user.setPassword(password);
		}
		return user;

	}

	@Override
	public List<User> getUserList() {
		return dao.findByRole("user");
		
	}

	@Override
	public void deleteUser(int userid) {
		dao.deleteById(userid);

	}

	@Override
	public User getUserById(String userid) throws Exception {
//		PasswordSecurity ps = new PasswordSecurity();
		User user = dao.getById(Integer.parseInt(userid));
		
		
		
//		String password = user.getPassword();
//		user.setPassword();
		
		return user;
	}

	@Override
	public String authenticateUserForForgetPass(String email, String s_que, String s_ans) {

		List<User> users = dao.findDistinctByEmail(email);

		String msg;
		if (users.size() == 0) {
			msg = "Email is not registered";
		} else {
			User user = users.get(0);
			if (user.getSecurity_answer().equals(s_ans) && user.getSecurity_question().equals(s_que)) {
			msg = null;
			} else {
			msg = "Wrong security question or answer";
		}
		}

		return msg;
	}

	@Override
	public String sendOTPMail(String email) throws MessagingException {

		String otp = DataUtility.generateOTP();

		EmailMessageBean emailbean = new EmailMessageBean();

		emailbean.setTo(email);
		emailbean.setSubject("OTP For Resetpassword");
		emailbean.setMessage("Your One Time Password is " + otp);

		EmailUtility emailUtility = new EmailUtility();
		emailUtility.sendMail(emailbean);
		
		return otp;
	}

	public void saveOTP(String email, String otp) {
		
		List<User> users =  dao.findDistinctByEmail(email);
		User user = users.get(0);
		
		user.setOtp(otp);
		
		dao.save(user);
	}

	@Override
	public void updateUser(User user) throws Exception {
		
//		PasswordSecurity ps = new PasswordSecurity();
//		String password = user.getPassword();
		
//		user.setPassword(ps.encrypt(password)); // encrypt
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		
		dao.save(user);
	}

	@Override
	public boolean verifyOtp(String email, String otp) {
		
		List<User> users = dao.findDistinctByEmail(email);
		User user = users.get(0);
		
		if(user.getOtp().equals(otp)) {
			return true;
		}
		return false;
	}

	@Override
	public void updatePssword(String email, String password) throws Exception {
		
//		PasswordSecurity ps = new PasswordSecurity();
//		password = ps.encrypt(password);
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		password = encoder.encode(password);
		
		List<User> users = dao.findDistinctByEmail(email);
		User user = users.get(0);
		
		
		user.setPassword(password);
		
		dao.save(user);
	}



}