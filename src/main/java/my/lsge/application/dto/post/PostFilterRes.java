package my.lsge.application.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.lsge.application.dto.BasePaginationFilterRes;

@Getter
@Setter
@NoArgsConstructor
public class PostFilterRes extends BasePaginationFilterRes<PostFilterItemRes> {
}
