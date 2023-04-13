package com.yumeng.webapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{

    @Autowired
    private DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Set SecurityFilterChain...");
        http.csrf().disable()
              .authorizeHttpRequests()
              .requestMatchers(HttpMethod.POST,"/v1/user").permitAll()
              .requestMatchers(HttpMethod.GET,"/healthz").permitAll()
//              .requestMatchers(HttpMethod.GET,"/health").permitAll()
              .requestMatchers(HttpMethod.GET,"/v1/product/**").permitAll()
              .anyRequest().authenticated()
              .and().httpBasic();
        logger.info("[SUCCESS]Set SecurityFilterChain SUCCESS.");
        return http.build();
    }


    @Bean
    UserDetailsService customUserDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
