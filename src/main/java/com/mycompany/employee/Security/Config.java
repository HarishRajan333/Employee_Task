package com.mycompany.employee.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Config {

    String admin = "ADMIN";
    String employee = "EMPLOYEE";
    String registory = "/api/register";
    String url = "/api/employees";

//    @Bean
//    public WebSecurityCustomizer customizer(HttpSecurity http) {
//        return (web) -> web.ignoring().requestMatchers(registory);
//    }
    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(a -> a.requestMatchers(HttpMethod.POST, url).hasRole(admin)
                .requestMatchers(HttpMethod.GET, url + "/**").hasAnyRole(admin, employee)
                .requestMatchers(HttpMethod.PUT, url + "/**").hasRole(admin)
                .requestMatchers(HttpMethod.DELETE, url + "/**").hasRole(admin)
                .requestMatchers(HttpMethod.POST, url + "/**").hasRole(employee)
                .requestMatchers(registory).permitAll()
                .anyRequest()
                .authenticated()
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
