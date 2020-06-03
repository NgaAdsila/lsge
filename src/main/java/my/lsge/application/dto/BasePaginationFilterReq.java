package my.lsge.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import my.lsge.application.Common.Const;
import my.lsge.domain.enums.SortByEnum;
import my.lsge.domain.enums.SortTypeEnum;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class BasePaginationFilterReq {
    private int page;

    private Integer limit;

    @JsonProperty("sort_by")
    private String sortBy;

    @JsonProperty("sort_type")
    private String sortType;

    public BasePaginationFilterReq() {
        this.page = Const.PAGE_DEFAULT;
        this.limit = Const.LIMIT_DAFAULT;
        this.sortBy = SortByEnum.createdAt.name();
        this.sortType = SortTypeEnum.DESC.name();
    }

    protected void normalize() {
        if (StringUtils.isNotEmpty(this.sortType)) {
            this.sortType = this.sortType.toUpperCase();
        }

        if (!SortTypeEnum.toList().contains(this.sortType)) {
            this.sortType = SortTypeEnum.DESC.name();
        }
    }
}
