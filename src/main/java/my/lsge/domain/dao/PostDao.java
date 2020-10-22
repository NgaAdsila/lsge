package my.lsge.domain.dao;

import my.lsge.application.dto.post.PostFilterReq;
import my.lsge.domain.entity.Post;

import java.util.List;

public interface PostDao {

    long filterCount(PostFilterReq req, List<Long> friendIds, Long userId);

    List<Post> filter(PostFilterReq req, List<Long> friendIds, Long userId);
}
