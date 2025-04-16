package com.example.TO_Do_List.To_Do_List.Services;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TO_Do_List.To_Do_List.Entity.Task;
import com.example.TO_Do_List.To_Do_List.Entity.User;
import com.example.TO_Do_List.To_Do_List.Repository.TaskRepo;

@Service
public class TaskService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TaskRepo taskRepo;

    //finding the Task associated with user
    public Task findByUser(String id) {
         return  taskRepo.findById(id).orElseThrow();
    }

    // saving the task in database
    public Task saveTask(Task task) {
        String id = UUID.randomUUID().toString();
        task.setId(id);
        return taskRepo.save(task);
    }

    // deleteng the task from databse
    public void delete(Task task) {
        taskRepo.delete(task);
    }


    // finding all the task related to the specified user
    public List<Task> findByUser(User authenticatedUser) {
        List<Task> tasks = taskRepo.findByUser(authenticatedUser);
        //logger.info("list of task are : {}" , tasks.toString());
        logger.info(tasks.toString());
        

        return tasks;
    }

    // finding a task associated with task id 
    public Task findById(String id) {
        Task task =  taskRepo.findById(id).orElseThrow() ;
        if(task != null)
        {
            return task;
    
        }
        return null;
    }

    // updating the task with new task with the help of id
    public Task updateTask(String id , Task modified) {

        Task existingTask = findById(id);
        
        existingTask.setDescription(modified.getDescription());
        existingTask.setCompleted(modified.isCompleted());

        return taskRepo.save(existingTask);

        

        
        
    }
    
}
