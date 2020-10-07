package my.lsge.application.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class UploadImageRes {
    private ImageRes data;
    private int status;
    private boolean success;

    public UploadImageRes() {
        this.success = false;
        this.status = HttpStatus.OK.value();
    }
}
