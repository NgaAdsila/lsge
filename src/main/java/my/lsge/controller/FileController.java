package my.lsge.controller;

import lombok.extern.slf4j.Slf4j;
import my.lsge.application.dto.admin.file.FileListReq;
import my.lsge.application.dto.admin.file.FileListRes;
import my.lsge.domain.logic.FileLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class FileController extends BaseController {

    @Autowired
    private FileLogic fileLogic;

    @PostMapping("/get-list")
    public FileListRes getList(@Valid @RequestBody FileListReq req) {
        return fileLogic.getList(req, getUserId());
    }

    @GetMapping("/read")
    @PreAuthorize("hasRole('ADMIN')")
    public void readFile(String filePath, HttpServletResponse response) {
        try {
            File file = new File(filePath);
            response.setContentType("application/octet-stream");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires",0);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            response.setHeader("Access-Control-Allow-Origin", "*");
            FileInputStream inputStream = new FileInputStream(file);
            byte[] data = new byte[inputStream.available()];
            ServletOutputStream out = response.getOutputStream();
            int length;
            while ((length = inputStream.read(data)) > 0) {
                out.write(data);
            }
            out.flush();
            inputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
