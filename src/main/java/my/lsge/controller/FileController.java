package my.lsge.controller;

import my.lsge.application.dto.admin.file.FileListReq;
import my.lsge.application.dto.admin.file.FileListRes;
import my.lsge.domain.logic.FileLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;

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
            FileInputStream inputStream = new FileInputStream(file);
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            response.getOutputStream().write(data);
            inputStream.close();
            response.setHeader("Access-Control-Allow-Origin", "*");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
