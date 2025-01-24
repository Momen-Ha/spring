package gzg.momen.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TasksPageResponse {
    private List<TaskResponse> data;
    private int page;
    private int limit;
    private int total;

}
