package com.yumeng.webapp.config;

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
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//          http.csrf().disable()
//                  .authorizeHttpRequests()
//                  .requestMatchers(HttpMethod.POST,"/v1/user").permitAll()
//                  .requestMatchers(HttpMethod.GET,"/healthz").permitAll()
//                  .anyRequest().authenticated()
//                  .and().httpBasic();
        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers(HttpMethod.PUT,"/v1/user/**").authenticated()
                .requestMatchers(HttpMethod.GET,"/v1/user/**").authenticated()
                .anyRequest().permitAll()
                .and().httpBasic();
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
