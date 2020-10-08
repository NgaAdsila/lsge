package my.lsge.application.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUploadRes {
    private String filePath;
    private String previewUrl;

    public FileUploadRes() {

    }

    public FileUploadRes(String filePath, String previewUrl) {
        this.filePath = filePath;
        this.previewUrl = previewUrl;
    }
}
