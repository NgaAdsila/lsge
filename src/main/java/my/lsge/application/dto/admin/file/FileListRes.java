package my.lsge.application.dto.admin.file;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FileListRes {
    private String root;
    private List<FileItemRes> files;

    public FileListRes() {
        this.files = new ArrayList<>();
    }
}
