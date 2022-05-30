package com.dhruvi.umsboot;


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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;




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
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		return provider;
		
	}
	
//	@Bean
//	public PasswordEncoder getPasswordEncoder() {
//		return NoOpPasswordEncoder.getInstance();
//	}
//	
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/js/**","/css/**","/library/**","/forgetpassword", "/registration",
					"/process_login","/process_form","/checkEmailAvailability",
					"/forgetPasswordController","/verifyOTPController","/updatePasswordController").permitAll()
			.antMatchers("/adminDashboard").hasRole("ADMIN")
			.antMatchers("/profile").hasRole("USER")
			.anyRequest().authenticated()
			.and()	
			.formLogin()
			.loginPage("/loginpage")
			.loginProcessingUrl("/login")
			.usernameParameter("email")
			.permitAll()
			.defaultSuccessUrl("/defaultLoginPath",true)
			.and()
			.logout()
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutRequestMatcher(new AntPathRequestMatcher("/logOutController"))
			.logoutSuccessUrl("/loginpage").permitAll();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(authProvider());
		auth.userDetailsService(userDetailsService);
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

