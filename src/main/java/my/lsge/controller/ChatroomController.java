package my.lsge.controller;

import my.lsge.application.dto.chatroom.*;
import my.lsge.domain.logic.ChatroomLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PostMapping("/create-message")
    public MessageRes createMessage(@Valid @RequestBody AddingMessageReq req) {
        return chatroomLogic.createMessage(req, getUserId());
    }

    @PutMapping("/is-read-message/{id}")
    public MessageRes isReadMessage(@PathVariable long id) {
        return chatroomLogic.isReadMessage(id, getUserId());
    }

    @PutMapping("/{id}")
    public MessageRes update(@PathVariable long id, @Valid @RequestBody UpdatingChatroomReq req) {
        return chatroomLogic.update(id, req, getUserId());
    }

    @PutMapping("/{id}/set-nickname")
    public MessageRes setNickname(@PathVariable long id, @RequestBody SettingNicknameReq req) {
        return chatroomLogic.setNickname(id, req, getUserId());
    }
}
