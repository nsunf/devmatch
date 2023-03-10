package com.devmatch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.sessionManagement()
    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		
		http.formLogin()
			.loginPage("/auth/login")
			.defaultSuccessUrl("/")
			.usernameParameter("email")
			.failureUrl("/auth/login/error")
			.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
			.logoutSuccessUrl("/");


		http.authorizeRequests()
			.mvcMatchers("/css/**", "/js/**", "/local/images/**", "/images/**", "/lib/**").permitAll()
			.mvcMatchers("/", "/about", "/partners/**", "/portfolios/**").permitAll()
			.mvcMatchers("/auth/signup", "/auth/login").anonymous()
			.anyRequest().authenticated();

		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
