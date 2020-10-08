package my.lsge.application.service;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.v2.files.CreateFolderResult;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;

import java.io.InputStream;

public interface DropboxService {
    InputStream downloadFile(String filePath);

    FileMetadata uploadFile(String filePath, InputStream fileStream);

    CreateFolderResult createFolder(String folderPath);

    FolderMetadata getFolderDetails(String folderPath);

    FileMetadata getFileDetails(String filePath);

    ListFolderResult listFolder(String folderPath, boolean recursiveListing, long limit);

    ListFolderResult listFolderContinue(String cursor);

    void deleteFile(String filePath);

    void deleteFolder(String folderPath);

    DbxDownloader<FileMetadata> getPreview(String filePath);

    SharedLinkMetadata createShareLink(String filePath);
}
