package my.lsge.application.dto.post;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.enums.PostShareModeEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AddingPostReq {
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String content;

    private boolean hasImage;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Size(max = 16)
    private PostShareModeEnum shareMode;
}
