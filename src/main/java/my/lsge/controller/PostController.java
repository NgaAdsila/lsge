package my.lsge.controller;

import my.lsge.application.dto.post.*;
import my.lsge.domain.entity.Comment;
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
    public PostRes add(@RequestBody AddingPostReq req) {
        return postLogic.add(req, getUserId());
    }

    @PutMapping("")
    public PostRes update(@RequestBody UpdatingPostReq req) {
        return postLogic.update(req, getUserId());
    }

    @PostMapping("/{id}/create-comment")
    public PostRes addComment(@PathVariable("id") Long id, @RequestBody AddingCommentReq req) {
        return postLogic.addComment(id, req, getUserId());
    }
}
