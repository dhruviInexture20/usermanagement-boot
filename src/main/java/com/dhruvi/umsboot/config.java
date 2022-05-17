package com.dhruvi.umsboot;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dhruvi.umsboot.filter.CustomFilter;
import com.dhruvi.umsboot.filter.LoginAuthFilter;

@Configuration
public class config {

	@Bean
	public FilterRegistrationBean < CustomFilter > customFilert(){
		
		FilterRegistrationBean < CustomFilter > registration = new FilterRegistrationBean<CustomFilter>();
		CustomFilter customFiler = new CustomFilter();
		
		registration.setFilter(customFiler);
		registration.addUrlPatterns("/adminDashboard", "/profile", "/editUserController");
		registration.setOrder(0);
		return registration;
	}	
	
	@Bean
	public FilterRegistrationBean<LoginAuthFilter> loginAuthentication(){
		
		FilterRegistrationBean<LoginAuthFilter> registration = new FilterRegistrationBean<LoginAuthFilter>();
		
		LoginAuthFilter authfilter = new LoginAuthFilter();
		
		registration.setFilter(authfilter);
		registration.addUrlPatterns("/"  ,"/login" );
		registration.setOrder(1);
		
		return registration;
	}
}
