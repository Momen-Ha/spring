package gzg.momen.todolist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    @NotNull
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be ≤ 100 characters")
    private String title;

    private Instant createDate;

    @NotNull
    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must be ≤ 500 characters")
    private String description;
}
