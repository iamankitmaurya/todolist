package com.example.TO_Do_List.To_Do_List.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.TO_Do_List.To_Do_List.Services.CustomUserDetailService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    private jwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF for APIs (useful for stateless REST APIs)
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers( "/api/auth/**").permitAll() // Public endpoints
                                .anyRequest().authenticated() // All other endpoints require authentication
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)// required jwt authetication
                .httpBasic(Customizer.withDefaults())
                .cors(Customizer.withDefaults()); // enabling cores through security chain

        return http.build();
    }

    


   @Bean
   // providing own authentication provider with the help of dao
public AuthenticationProvider authenticationProvider()
{
     DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
     provider.setPasswordEncoder(passwordEncoder());
     provider.setUserDetailsService(customUserDetailService);
     return provider;
}

@Bean
// authentication manager who uses dao for authenticating user in backend 
public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = 
        http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(authenticationProvider()); // Add the authentication provider
    return authenticationManagerBuilder.build();
}
//  @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration corsConfiguration = new CorsConfiguration();
//         corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000")); // React app URL
//         corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//         corsConfiguration.setAllowedHeaders(List.of("*"));
//         corsConfiguration.setAllowCredentials(true);

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", corsConfiguration);

//         return source;
//     }


    
    
}
