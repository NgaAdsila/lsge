package my.lsge.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BasePaginationFilterRes<T> {

    @JsonIgnore
    private long count;

    private List<T> responses;

    private PagingRes paging;

    public BasePaginationFilterRes() {
        this.count = 0;
        this.responses = new ArrayList<>();
        this.paging = new PagingRes();
    }

    public void setPaging(BasePaginationFilterReq req) {
        paging.setPage(req.getPage());
        paging.setLimit(req.getLimit());
        paging.setNumber(this.responses.size());
        paging.setTotal(this.count);
        paging.setSortBy(req.getSortBy());
        paging.setSortType(req.getSortType());
    }
}
