package com.example.TO_Do_List.To_Do_List.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TO_Do_List.To_Do_List.Entity.User;
import com.example.TO_Do_List.To_Do_List.Security.JwtUtil;
import com.example.TO_Do_List.To_Do_List.Services.UserServices;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class UserController {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserServices userServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;


    // registering the user through their credentials enetr in front end react
   @PostMapping("/register")
    public User register(@RequestBody User user) {
    // Check if the username already exists
    if (userServices.existsByUsername(user.getUsername())) {
        return null;
    }
    
    // Encrypt the user's password
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    
    // Save the user
       User user1 = userServices.saveUser(user);

       // Return a success message
    return user1;
}


//logging the uuser into systrem and giving hime jwt token for further communication
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        // it return token created with credentials
        return jwtUtil.generateToken(user.getUsername());
    }
    
    
}
