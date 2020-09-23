package my.lsge.controller;

import my.lsge.application.dto.ListObjectRes;
import my.lsge.domain.entity.User;
import my.lsge.domain.logic.RelationshipLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relationships")
public class RelationshipController extends BaseController {
    @Autowired
    private RelationshipLogic relationshipLogic;

    @GetMapping("/friend-list")
    public ListObjectRes<User> getFriendList() {
        return relationshipLogic.getFriendList(getUserId());
    }
}
