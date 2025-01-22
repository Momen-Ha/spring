package gzg.momen.todolist.dto;

import gzg.momen.todolist.entity.Task;
import lombok.Data;

import java.util.List;

@Data
public class TasksResponse {
    private List<TaskDTO> data;
    private int page;
    private int limit;
    private int total;

}
