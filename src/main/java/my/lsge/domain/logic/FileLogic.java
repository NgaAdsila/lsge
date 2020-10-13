package my.lsge.domain.logic;

import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import lombok.extern.slf4j.Slf4j;
import my.lsge.application.dto.admin.file.FileItemRes;
import my.lsge.application.dto.admin.file.FileListReq;
import my.lsge.application.dto.admin.file.FileListRes;
import my.lsge.application.dto.user.FileUploadRes;
import my.lsge.application.exception.NotFoundException;
import my.lsge.application.service.DropboxService;
import my.lsge.domain.enums.UserRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
@Component
public class FileLogic extends BaseLogic {
    @Autowired
    private DropboxService dropboxService;

    protected FileUploadRes upload(MultipartFile file, String filePath) throws IOException {
        InputStream in = file.getInputStream();
        dropboxService.uploadFile(filePath, in);
        SharedLinkMetadata sharedLink = dropboxService.createShareLink(filePath);
        if (sharedLink == null) {
            throw new RuntimeException();
        }
        return new FileUploadRes(filePath, sharedLink.getUrl().replace("dl=0", "raw=1"));
    }

    protected void delete(String filePath) {
        dropboxService.deleteFile(filePath);
    }

    public FileListRes getList(FileListReq req, Long userId) {
        validateUser(userId, UserRoleEnum.ROLE_ADMIN);
        req.normalize();
        try (Stream<Path> paths = Files.walk(Paths.get(req.getFilePath()))) {
            FileListRes res = new FileListRes();
            res.setRoot(req.getFilePath());
            paths.forEach(path -> {
                FileItemRes item = FileItemRes.by(path, req.getFilePath());
                if (item != null) {
                    res.getFiles().add(item);
                }
            });
            return res;
        } catch (Exception e) {
            throw new NotFoundException(language.getString("file.not_found"));
        }
    }
}
