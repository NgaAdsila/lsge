package my.lsge.application.dto.post;

import lombok.Getter;
import lombok.Setter;
import my.lsge.domain.enums.PostShareModeEnum;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdatingPostReq {
    private long id;

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Size(max = 16)
    private PostShareModeEnum shareMode;

    @Size(max = 255)
    private String shareTitle;

    public void normalize() {
        if (StringUtils.isNotBlank(this.title)) {
            this.title = this.title.trim();
        }
        if (StringUtils.isNotBlank(this.content)) {
            this.content = this.content.trim();
        }
        if (StringUtils.isNotBlank(this.shareTitle)) {
            this.shareTitle = this.shareTitle.trim();
        }
    }
}
