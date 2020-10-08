package my.lsge.application.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageRes {
    private String id;
    private String title;
    private String ul_viewer;
    private String url;
    private String display_url;
    private Long size;
    private Long time;
    private Long expiration;
    private ImageItemRes image;
    private ImageItemRes thumb;
    private String delete_url;
}
