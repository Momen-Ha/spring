package gzg.momen.todolist.controller;


import gzg.momen.todolist.dto.TaskDTO;
import gzg.momen.todolist.dto.TasksResponse;
import gzg.momen.todolist.entity.Task;
import gzg.momen.todolist.exceptions.TaskNotFoundException;
import gzg.momen.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/vi/todos")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Task> createTask(@RequestBody @Validated TaskDTO task,
                                           @AuthenticationPrincipal UserDetails user) {

        return taskService.createNewTask(task, user);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> updateTask(@RequestBody @Validated TaskDTO task,
                                        @PathVariable Long id,
                                        @AuthenticationPrincipal UserDetails user) {
        return taskService.updateTask(task, id, user);
    }



    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> deleteTask(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetails user) {
            return taskService.deleteTask(id, user);
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<TasksResponse> getAllTasks(
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "limit", defaultValue = "10", required = false) int pageSize,
            @AuthenticationPrincipal UserDetails user
    ) {
        TasksResponse tasks = taskService.getTasks(pageNo, pageSize, user);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}
