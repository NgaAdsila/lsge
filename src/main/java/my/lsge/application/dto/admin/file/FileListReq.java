package my.lsge.application.dto.admin.file;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FileListReq {

    @NotNull
    private String filePath;

    public void normalize() {
        if (StringUtils.isNotBlank(this.filePath)) {
            this.filePath = this.filePath.trim();
        }
    }
}
