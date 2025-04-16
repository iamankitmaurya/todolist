package com.example.TO_Do_List.To_Do_List.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TO_Do_List.To_Do_List.Entity.User;


@Repository
// initializing the user repository with the jpa repository
public interface UserRepo  extends JpaRepository<User , String>{
    User findByUsername(String username);
    public boolean existsByUsername(String Username);


}
