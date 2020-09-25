package my.lsge.application.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class FindUserReq {
    private String keyword;

    public void normalize() {
        if (StringUtils.isNotBlank(this.keyword)) {
            this.keyword = this.keyword.trim();
        }
    }
}
