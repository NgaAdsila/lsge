package my.lsge.application.dto.admin.file;

import lombok.Getter;
import lombok.Setter;

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
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1))
                .orElse(null);
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
