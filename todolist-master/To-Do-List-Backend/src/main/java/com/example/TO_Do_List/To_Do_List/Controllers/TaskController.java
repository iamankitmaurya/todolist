package com.example.TO_Do_List.To_Do_List.Controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TO_Do_List.To_Do_List.Entity.Task;
import com.example.TO_Do_List.To_Do_List.Entity.User;
import com.example.TO_Do_List.To_Do_List.Security.JwtUtil;
import com.example.TO_Do_List.To_Do_List.Services.TaskService;
import com.example.TO_Do_List.To_Do_List.Services.UserServices;

@RestController
@RequestMapping("/api/task")
public class TaskController {
     
    @Autowired
    private TaskService taskServices;

    

    @Autowired
    private UserServices userServices;

    @Autowired
    private JwtUtil jwtUtil;

    // logger for logging 
    Logger logger = LoggerFactory.getLogger(this.getClass());

    // finding the user associated with authentication bearer(jwt token)
    private User getAuthenticatedUser(String token) {
       try {
        logger.info("Token received: " + token);
        String username = jwtUtil.extractUsername(token.substring(7)); // Remove "Bearer " prefix
          return userServices.findByUsername(username);
        }
        catch(Exception e)
        {
            logger.error("error message fduring task controller : {}", e);
        }
        return null;

    }


     //getting the List of task with the help of user 
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@RequestHeader("Authorization") String token) {
        User user = getAuthenticatedUser(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<Task> tasks = taskServices.findByUser(user);
        logger.info(tasks.toString());
        
        return ResponseEntity.ok(tasks);
    }


    // adding the task  with post method
    @PostMapping
    public ResponseEntity<Task> addTask(@RequestBody Task task, @RequestHeader("Authorization") String token) {
        logger.info("Token received: " + token);
        User user = getAuthenticatedUser(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        task.setUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskServices.saveTask(task));
    }


    // deleteing the task with help of id comes in parameter
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id, @RequestHeader("Authorization") String token) {
        User user = getAuthenticatedUser(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Task task = taskServices.findById(id);
        if (task == null ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Forbidden if task doesn't belong to the user
        }

        taskServices.delete(task);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody Task updatedTask) {
        
        
        Task savedTask = taskServices.updateTask(id , updatedTask);
        return ResponseEntity.ok(savedTask);  // Return updated task
    }
}
