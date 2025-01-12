package gzg.momen.todolist.dto;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class UserDTO {

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String password;
}
