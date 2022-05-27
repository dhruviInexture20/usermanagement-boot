package com.dhruvi.umsboot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;




@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
//    String[] permitted = new String[]{
//            "/", "/home","/register","/about","/png/**",
//            "/css/**","/icons/**","/img/**","/js/**","/layer/**"};

	
	@Bean
	public AuthenticationProvider authProvider() {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		
		return provider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
//			.authorizeRequests().antMatchers("/login").permitAll()
//			.and()
			.authorizeRequests()
			.antMatchers("/js/**","/css/**","/forgetpassword", "/registration","/process_login").permitAll()
			.antMatchers("/adminDashboard").hasRole("ADMIN")
//			.access("hasRole('admin')")
			.antMatchers("/profile").hasRole("USER")
//			.access("hasRole('user')")
			.anyRequest().authenticated()
			.and()	
			.formLogin()
			.loginPage("/login")
			.permitAll()
			.defaultSuccessUrl("/profile",true)
			.and()
			.logout()
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutRequestMatcher(new AntPathRequestMatcher("/logOutController"))
			.logoutSuccessUrl("/login").permitAll();
	
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
	}
	
	
	
	
}



//@Bean
//@Override
//protected UserDetailsService userDetailsService() {
//	
//	
//	List<UserDetails> users = new ArrayList<>();
//	
//	users.add(User.withDefaultPasswordEncoder().username("dhruvi").password("123").roles("USER").build());
//	
//	return new InMemoryUserDetailsManager(users);
//	
//}

