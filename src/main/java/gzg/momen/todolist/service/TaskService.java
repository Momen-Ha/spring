package gzg.momen.todolist.service;


import gzg.momen.todolist.dto.TaskDTO;
import gzg.momen.todolist.dto.TaskMapper;
import gzg.momen.todolist.dto.TaskResponse;
import gzg.momen.todolist.entity.Task;
import gzg.momen.todolist.entity.TaskPage;
import gzg.momen.todolist.entity.TaskSearchCriteria;
import gzg.momen.todolist.entity.User;
import gzg.momen.todolist.repository.TaskCriteriaRepository;
import gzg.momen.todolist.repository.TaskRepository;
import gzg.momen.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskCriteriaRepository taskCriteriaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;

    public User findUserByEmail(UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return user;
    }

    public TaskResponse createNewTask(TaskDTO taskDTO, UserDetails userDetails) {
        User user = findUserByEmail(userDetails);
        Task task = new Task();
        task.setUser(user);
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setCreateDate(Instant.now());
        taskRepository.save(task);
        return taskMapper.taskToTaskResponse(task);
    }

    public TaskResponse updateTask(TaskDTO task, Long taskId, UserDetails userDetails) {
        User user = findUserByEmail(userDetails);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found!"));
        if(!existingTask.getUser().getUserId().equals(user.getUserId())) {
            throw new SecurityException("not authorized");
        }
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        taskRepository.save(existingTask);

        return  taskMapper.taskToTaskResponse(existingTask);
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

    public Page<TaskResponse> getTasks(TaskPage taskPage, TaskSearchCriteria taskSearchCriteria, UserDetails userDetails) {
        User user = findUserByEmail(userDetails);
        return taskCriteriaRepository.findAllWithFilters(taskPage, taskSearchCriteria, user.getUserId());

    }

}