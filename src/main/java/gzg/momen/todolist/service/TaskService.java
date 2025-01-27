package gzg.momen.todolist.service;


import gzg.momen.todolist.dto.TaskDTO;
import gzg.momen.todolist.dto.TasksResponse;
import gzg.momen.todolist.entity.Task;
import gzg.momen.todolist.entity.User;
import gzg.momen.todolist.exceptions.TaskNotFoundException;
import gzg.momen.todolist.repository.TaskRepository;
import gzg.momen.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;
    
    public User findUserByEmail(UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return user;
    }

    public ResponseEntity<Task> createNewTask(TaskDTO taskDTO, UserDetails userDetails) {
        User user = findUserByEmail(userDetails);
        Task task = new Task();
        task.setUser(user);
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        try {
            taskRepository.save(task);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    public ResponseEntity<Task> updateTask(TaskDTO task, Long taskId, UserDetails userDetails) {
        User user = findUserByEmail(userDetails);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task Not Found"));
        if(!existingTask.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("user not authorized to update");
        }
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        taskRepository.save(existingTask);
        return ResponseEntity.ok(existingTask);
    }

    public ResponseEntity<?> deleteTask(Long taskId, UserDetails userDetails) {
        User user = findUserByEmail(userDetails);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found!"));
        if(!existingTask.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("not authorized");
        }
        try {
            taskRepository.delete(existingTask);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public TasksResponse getTasks(int pageNo, int pageSize , UserDetails userDetails) {
        User user = findUserByEmail(userDetails);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Task> tasks = taskRepository.findAllByUser_UserId(user.getUserId(), pageable);
        List<Task> listOfTasks = tasks.getContent();

        TasksResponse tasksResponse = new TasksResponse();
        tasksResponse.setData(listOfTasks);
        tasksResponse.setPage(tasks.getNumber());
        tasksResponse.setLimit(tasks.getSize());
        tasksResponse.setTotal(tasks.getNumberOfElements());
        return tasksResponse;
    }
}
