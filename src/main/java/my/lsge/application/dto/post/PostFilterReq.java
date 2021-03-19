package my.lsge.application.dto.post;

import lombok.Getter;
import lombok.Setter;
import my.lsge.application.dto.BasePaginationFilterReq;
import my.lsge.domain.enums.SortByEnum;
import my.lsge.domain.enums.SortTypeEnum;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class PostFilterReq extends BasePaginationFilterReq {

    private String keyword;

    private Long lastId;

    public void normalize() {
        this.setSortBy(SortByEnum.id.name());
        this.setSortType(SortTypeEnum.DESC.name());

        if (StringUtils.isNotBlank(this.keyword)) {
            this.keyword = this.keyword.trim();
        }

        if (this.lastId == null || this.lastId < 0) {
            this.lastId = -1L;
        }

        super.normalize();
    }
}
