package my.lsge.controller;

import my.lsge.application.dto.ListObjectRes;
import my.lsge.application.dto.relation.AddingFriendReq;
import my.lsge.application.dto.relation.FriendItemRes;
import my.lsge.application.dto.relation.UpdatingFriendStatusReq;
import my.lsge.domain.logic.RelationshipLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/relationships")
public class RelationshipController extends BaseController {
    @Autowired
    private RelationshipLogic relationshipLogic;

    @GetMapping("/friend-list")
    public ListObjectRes<FriendItemRes> getFriendList() {
        return relationshipLogic.getFriendList(getUserId());
    }

    @PostMapping("")
    public void addFriend(@Valid @RequestBody AddingFriendReq req) {
        relationshipLogic.addFriend(req, getUserId());
    }

    @PutMapping("/approve")
    public void approve(@Valid @RequestBody UpdatingFriendStatusReq req) {
        relationshipLogic.approve(req, getUserId());
    }

    @PutMapping("/cancel")
    public void cancel(@Valid @RequestBody UpdatingFriendStatusReq req) {
        relationshipLogic.cancel(req, getUserId());
    }
}
