package my.lsge.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionRes {
    private Long value;
    private String text;

    public OptionRes() {

    }

    public OptionRes(Long value, String text) {
        this.value = value;
        this.text = text;
    }
}
