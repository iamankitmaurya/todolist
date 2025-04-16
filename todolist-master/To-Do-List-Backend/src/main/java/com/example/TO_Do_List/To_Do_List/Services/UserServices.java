package com.example.TO_Do_List.To_Do_List.Services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TO_Do_List.To_Do_List.Entity.User;
import com.example.TO_Do_List.To_Do_List.Repository.UserRepo;

@Service
public class UserServices {

    @Autowired
    UserRepo userRepo;


    // saving user in databse
    public User saveUser(User user) {
        // generating the user id for user
        String id = UUID.randomUUID().toString();
        user.setId(id);
        User save = userRepo.save(user);
        return save;
    }


    //checking user is exist or not   with username 
    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }


    // extracting user with username 
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
    
}
