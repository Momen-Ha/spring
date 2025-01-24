package gzg.momen.todolist.dto;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    @NotNull
    private String title;

    @NotNull
    private Instant creationDate;

    @NotNull
    private String description;
}
