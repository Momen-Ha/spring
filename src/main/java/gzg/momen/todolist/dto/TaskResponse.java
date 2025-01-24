package gzg.momen.todolist.dto;

import java.time.Instant;

public record TaskResponse (
        String title,
        String description,
        Instant createDate
){

}
