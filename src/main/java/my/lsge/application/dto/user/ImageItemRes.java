package my.lsge.application.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageItemRes {
    private String filename;
    private String name;
    private String mime;
    private String extension;
    private String url;
}
