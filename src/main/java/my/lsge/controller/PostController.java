package my.lsge.controller;

import my.lsge.application.dto.post.AddingPostReq;
import my.lsge.application.dto.post.PostFilterReq;
import my.lsge.application.dto.post.PostFilterRes;
import my.lsge.application.dto.post.UpdatingPostReq;
import my.lsge.domain.entity.Post;
import my.lsge.domain.logic.PostLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController extends BaseController {

    @Autowired
    private PostLogic postLogic;

    @PostMapping("/filter")
    public PostFilterRes filter(@RequestBody PostFilterReq req) {
        return postLogic.filter(req, getUserId());
    }

    @PostMapping("")
    public Post add(@RequestBody AddingPostReq req) {
        return postLogic.add(req, getUserId());
    }

    @PutMapping("")
    public Post update(@RequestBody UpdatingPostReq req) {
        return postLogic.update(req, getUserId());
    }
}
