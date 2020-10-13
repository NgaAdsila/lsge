package my.lsge.application.dto.admin.file;

import lombok.Getter;
import lombok.Setter;
import my.lsge.application.common.Const;

import javax.validation.constraints.NotNull;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Getter
@Setter
public class FileItemRes {
    private String name;
    private String extension;
    private String path;
    private boolean isFolder;
    private String title;
    private int deep;

    public static FileItemRes by(Path path, String rootPath) {
        boolean isFolder = Files.isDirectory(path);
        String fileName = path.getFileName().toString();
        String extension = isFolder ? null : Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.toLowerCase().lastIndexOf(".") + 1))
                .orElse(null);
        if (extension != null && !Const.TEXT_FILE_EXTENSIONS.contains(extension)) {
            return null;
        }
        String title = path.toString().replace(rootPath, "ROOT");
        int deep = title.split("/").length - 1;

        FileItemRes res = new FileItemRes();
        res.setName(fileName);
        res.setExtension(extension);
        res.setPath(path.toString());
        res.setFolder(isFolder);
        res.setTitle(title);
        res.setDeep(deep);
        return res;
    }
}
