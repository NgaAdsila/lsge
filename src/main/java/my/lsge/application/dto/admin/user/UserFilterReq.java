package my.lsge.application.dto.admin.user;

import lombok.Getter;
import lombok.Setter;
import my.lsge.application.dto.BasePaginationFilterReq;
import my.lsge.domain.enums.SortByEnum;
import my.lsge.domain.enums.SortTypeEnum;
import my.lsge.util.Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserFilterReq extends BasePaginationFilterReq {

    private String keyword;

    private List<Long> roles;

    private Boolean status;

    public UserFilterReq() {
        this.roles = new ArrayList<>();
    }

    public void normalize() {
        this.setSortBy(SortByEnum.createdAt.name());
        this.setSortType(SortTypeEnum.DESC.name());
        super.normalize();

        if (StringUtils.isNotBlank(this.keyword)) {
            this.keyword = this.keyword.trim();
        }

        if (!Utils.isNullOrEmpty(this.roles)) {
            this.roles = Utils.removeNull(this.roles);
        }
    }
}
