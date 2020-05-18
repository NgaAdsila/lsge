package my.lsge.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListObjectRes<T> {

    private List<T> responses;

    public ListObjectRes() {
        this.responses = new ArrayList<>();
    }
}
