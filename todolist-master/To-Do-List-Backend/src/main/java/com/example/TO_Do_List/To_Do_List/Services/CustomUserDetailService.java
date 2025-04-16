package com.example.TO_Do_List.To_Do_List.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.TO_Do_List.To_Do_List.Entity.User;
import com.example.TO_Do_List.To_Do_List.Repository.UserRepo;

@Service
// implementing the user details service to authenticate user in dao 
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findByUsername(username);

        return  org.springframework.security.core.userdetails.User.builder()
              .username(user.getUsername())
              .password(user.getPassword())  // Assuming the password is already encoded
              .build();


        
    }

}
