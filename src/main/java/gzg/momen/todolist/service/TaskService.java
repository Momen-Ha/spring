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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskCriteriaRepository taskCriteriaRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskCriteriaRepository taskCriteriaRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskCriteriaRepository = taskCriteriaRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    public User findUserByEmail(UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("user with email: " + userDetails.getUsername() + " not found");
        }
        return user;
    }

    @Transactional
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

    @Transactional
    public TaskResponse updateTask(TaskDTO task, Long taskId, UserDetails userDetails) throws AccessDeniedException {
        User user = findUserByEmail(userDetails);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));
        if(!existingTask.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("User " + user.getUserId() + " not authorized to delete task " + taskId);
        }
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        taskRepository.save(existingTask);

        return  taskMapper.taskToTaskResponse(existingTask);
    }

    @Transactional
    public void deleteTask(Long taskId, UserDetails userDetails) throws AccessDeniedException {
        User user = findUserByEmail(userDetails);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));

        if(!existingTask.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("User " + user.getUserId() + " not authorized to delete task " + taskId);        }
        taskRepository.delete(existingTask);
    }

    public Page<TaskResponse> getTasks(TaskPage taskPage, TaskSearchCriteria taskSearchCriteria, UserDetails userDetails) {
        taskPage = Optional.ofNullable(taskPage).orElse(new TaskPage());
        taskSearchCriteria = Optional.ofNullable(taskSearchCriteria).orElse(new TaskSearchCriteria());
        User user = findUserByEmail(userDetails);
        return taskCriteriaRepository.findAllWithFilters(taskPage, taskSearchCriteria, user.getUserId());

    }

}