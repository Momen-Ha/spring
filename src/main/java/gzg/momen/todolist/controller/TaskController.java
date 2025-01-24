package gzg.momen.todolist.controller;


import gzg.momen.todolist.dto.TaskDTO;
import gzg.momen.todolist.dto.TaskResponse;
import gzg.momen.todolist.dto.TasksPageResponse;
import gzg.momen.todolist.entity.TaskPage;
import gzg.momen.todolist.entity.TaskSearchCriteria;
import gzg.momen.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/todos")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Validated TaskDTO task,
                                           @AuthenticationPrincipal UserDetails user) {

        try {
            TaskResponse createdTask = taskService.createNewTask(task, user);
            return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> updateTask(@RequestBody @Validated TaskDTO task,
                                        @PathVariable Long id,
                                        @AuthenticationPrincipal UserDetails user) {
        try {
            TaskResponse updatedTask = taskService.updateTask(task, id, user);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (SecurityException e) {
            return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }



    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> deleteTask(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetails user) {
        try {
            taskService.deleteTask(id, user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (SecurityException e) {
            return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<TasksPageResponse> getAllTasks(
            TaskPage taskPage,
            TaskSearchCriteria taskSearchCriteria,
            @AuthenticationPrincipal UserDetails user
    ) {

        Page<TaskResponse> listOfTasks = taskService.getTasks(taskPage, taskSearchCriteria, user);

        TasksPageResponse tasks = new TasksPageResponse();
        tasks.setData(listOfTasks.getContent());
        tasks.setPage(listOfTasks.getNumber());
        tasks.setLimit(listOfTasks.getSize());
        tasks.setTotal(listOfTasks.getContent().size());
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

}