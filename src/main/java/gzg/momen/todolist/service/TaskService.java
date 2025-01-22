package gzg.momen.todolist.service;


import gzg.momen.todolist.dto.TaskDTO;
import gzg.momen.todolist.dto.TasksResponse;
import gzg.momen.todolist.entity.Task;
import gzg.momen.todolist.entity.User;
import gzg.momen.todolist.repository.TaskRepository;
import gzg.momen.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Task createNewTask(TaskDTO taskDTO, UserDetails userDetails) {
        User user = findUserByEmail(userDetails);
        Task task = new Task();
        task.setUser(user);
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());

        return taskRepository.save(task);
    }

    public Task updateTask(TaskDTO task, Long taskId, UserDetails userDetails) {
        User user = findUserByEmail(userDetails);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found!"));
        if(!existingTask.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("not authorized");
        }
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long taskId, UserDetails userDetails) {
        User user = findUserByEmail(userDetails);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found!"));
        if(!existingTask.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("not authorized");
        }
        taskRepository.delete(existingTask);
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
