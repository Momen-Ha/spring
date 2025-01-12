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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Task createNewTask(TaskDTO taskDTO) {
        Task task = new Task();
        User user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));
        task.setUser(user);
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());

        return taskRepository.save(task);
    }

    public Task updateTask(TaskDTO task, Long taskId) {
        Task updatedTask = taskRepository.findById(taskId).orElse(null);
        task.setTitle(task.getTitle());
        task.setDescription(task.getDescription());
        taskRepository.save(updatedTask);
        return updatedTask;
    }

    public Boolean deleteTask(Long taskId) {
        if(taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
            return true;
        }
        return false;
    }

    public TasksResponse getTasks(int pageNo, int pageSize , Long userId) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Task> tasks = taskRepository.findAllByUser_UserId(userId, pageable);
        List<Task> listOfTasks = tasks.getContent();

        TasksResponse tasksResponse = new TasksResponse();
        tasksResponse.setData(listOfTasks);
        tasksResponse.setPage(tasks.getNumber());
        tasksResponse.setLimit(tasks.getSize());
        tasksResponse.setTotal(tasks.getNumberOfElements());
        return tasksResponse;
    }
}
