package my.lsge.application.dto.loginHistory;

import lombok.Getter;
import lombok.Setter;
import my.lsge.application.dto.BasePaginationFilterReq;
import my.lsge.domain.enums.SortByEnum;
import my.lsge.domain.enums.SortTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

@Getter
@Setter
public class LoginHistoryFilterReq extends BasePaginationFilterReq {

    private String keyword;

    private LocalDate timeFrom;

    private LocalDate timeTo;

    public void normalize() {
        this.setSortBy(SortByEnum.createdAt.name());
        this.setSortType(SortTypeEnum.DESC.name());
        super.normalize();

        if (StringUtils.isNotBlank(this.keyword)) {
            this.keyword = this.keyword.trim();
        }
    }
}
