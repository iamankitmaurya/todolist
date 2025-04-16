package com.example.TO_Do_List.To_Do_List.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.TO_Do_List.To_Do_List.Entity.Task;
import com.example.TO_Do_List.To_Do_List.Entity.User;

@Repository
// reposiitory iinitialization with jpa
public interface TaskRepo extends JpaRepository<Task , String> {
    @Query("SELECT t FROM Task t WHERE t.user = :user")
    List<Task> findByUser(@Param("user") User user);
    
}
