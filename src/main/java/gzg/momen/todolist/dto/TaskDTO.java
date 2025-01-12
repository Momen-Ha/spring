package gzg.momen.todolist.dto;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class TaskDTO {

    @NotNull
    private int userId;

    @NotNull
    private String title;

    @NotNull
    private String description;
}
