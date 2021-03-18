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
    public AddingPostRes add(@RequestBody AddingPostReq req) {
        return postLogic.add(req, getUserId());
    }

    @PutMapping("")
    public PostRes update(@RequestBody UpdatingPostReq req) {
        return postLogic.update(req, getUserId());
    }

    @PostMapping("/{id}/create-comment")
    public PostRes addComment(@PathVariable("id") long id, @RequestBody AddingCommentReq req) {
        return postLogic.addComment(id, req, getUserId());
    }

    @GetMapping("/{id}")
    public PostDetailRes getById(@PathVariable("id") long id) {
        return postLogic.getById(id, getUserId());
    }

    @PostMapping("/{id}/like")
    public PostRes like(@PathVariable("id") long id) {
        return postLogic.like(id, getUserId());
    }

    @PostMapping("/{id}/dislike")
    public PostRes dislike(@PathVariable("id") long id) {
        return postLogic.dislike(id, getUserId());
    }

    @PutMapping("/{id}")
    public PostRes delete(@PathVariable("id") long id) {
        return postLogic.delete(id, getUserId());
    }

    @PostMapping("/{id}/reply-comment")
    public ReplyCommentRes replyComment(@PathVariable("id") long id, @RequestBody ReplyCommentReq req) {
        return postLogic.replyComment(id, req, getUserId());
    }

    @PostMapping("/{id}/edit-comment")
    public ReplyCommentRes editComment(@PathVariable("id") long id, @RequestBody ReplyCommentReq req) {
        return postLogic.editComment(id, req, getUserId());
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    public ReplyCommentRes deleteComment(@PathVariable("id") long id, @PathVariable("commentId") long commentId) {
        return postLogic.deleteComment(id, commentId, getUserId());
    }
}
