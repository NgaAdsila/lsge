package my.lsge.controller;

import my.lsge.application.dto.chatroom.ChatroomRes;
import my.lsge.application.dto.chatroom.InitChatroomReq;
import my.lsge.domain.logic.ChatroomLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatroomController extends BaseController {

    @Autowired
    private ChatroomLogic chatroomLogic;

    @PostMapping("/init-normal")
    public ChatroomRes initNormalChatroom(@RequestBody InitChatroomReq req) {
        return chatroomLogic.initNormalChatroom(req, getUserId());
    }

    @GetMapping("/{id}")
    public ChatroomRes findById(@PathVariable long id) {
        return chatroomLogic.findById(id, getUserId());
    }
}
