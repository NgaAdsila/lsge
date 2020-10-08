package my.lsge.domain.logic;

import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import lombok.extern.slf4j.Slf4j;
import my.lsge.application.dto.user.FileUploadRes;
import my.lsge.application.service.DropboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

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
}
