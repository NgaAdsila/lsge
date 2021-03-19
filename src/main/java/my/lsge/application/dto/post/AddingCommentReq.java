package my.lsge.application.dto.post;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AddingCommentReq {

    @NotBlank
    @Size(max = 2000)
    private String message;
}
