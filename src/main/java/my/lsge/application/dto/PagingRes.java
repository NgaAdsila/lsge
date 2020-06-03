package my.lsge.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingRes {

    private int page;

    private int limit;

    private int number;

    private long total;

    @JsonProperty("sort_by")
    private String sortBy;

    @JsonProperty("sort_type")
    private String sortType;
}
