package gzg.momen.todolist.dto;

import gzg.momen.todolist.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskResponse taskToTaskResponse(Task task);

    Task taskResponseToTask(TaskResponse taskResponse);
}
