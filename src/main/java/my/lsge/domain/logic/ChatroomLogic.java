package my.lsge.domain.logic;

import my.lsge.application.dto.chatroom.AddingMessageReq;
import my.lsge.application.dto.chatroom.ChatroomRes;
import my.lsge.application.dto.chatroom.InitChatroomReq;
import my.lsge.application.dto.chatroom.MessageRes;
import my.lsge.application.exception.ForbiddenException;
import my.lsge.application.exception.FormValidationException;
import my.lsge.application.exception.NotFoundException;
import my.lsge.domain.dao.ChatroomDao;
import my.lsge.domain.entity.*;
import my.lsge.domain.enums.ChatroomStatusEnum;
import my.lsge.domain.enums.ChatroomTypeEnum;
import my.lsge.domain.enums.ChatroomUserStatusEnum;
import my.lsge.domain.repository.ChatroomRepository;
import my.lsge.domain.repository.ChatroomUserRepository;
import my.lsge.domain.repository.MessageRepository;
import my.lsge.domain.repository.MessageTrackingStatusRepository;
import my.lsge.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatroomLogic extends BaseLogic {

    @Autowired
    private ChatroomDao chatroomDao;

    @Autowired
    private ChatroomRepository chatroomRepository;

    @Autowired
    private ChatroomUserRepository chatroomUserRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageTrackingStatusRepository messageTrackingStatusRepository;

    public ChatroomRes initNormalChatroom(InitChatroomReq req, Long userId) {
        User user = userRepository.getOne(userId);
        validateUser(user);

        if (userId.equals(req.getUserId())) {
            throw new FormValidationException(language.getString("chatroom.yourself"));
        }

        User recUser = userRepository.findById(req.getUserId()).orElse(null);
        if (recUser == null || recUser.isDeleted()) {
            throw new FormValidationException(language.getString("valid.user.is_not_existed"));
        }

       Chatroom chatroom = chatroomDao.findByTypeAndUsers(ChatroomTypeEnum.NORMAL, Arrays.asList(userId, req.getUserId()));
       if (chatroom == null) {
           chatroom = new Chatroom(0L, req.getName(), ChatroomTypeEnum.NORMAL, null, ChatroomStatusEnum.CREATED);

           ChatroomUser reqChatroomUser = new ChatroomUser(0L, chatroom, user, null, ChatroomUserStatusEnum.JOINING);
           ChatroomUser recChatroomUser = new ChatroomUser(0L, chatroom, recUser, null, ChatroomUserStatusEnum.JOINING);

           chatroomRepository.save(chatroom);
           chatroomUserRepository.save(reqChatroomUser);
           chatroomUserRepository.save(recChatroomUser);
       }

        return ChatroomRes.by(chatroom, false, null);
    }

    public ChatroomRes findById(long id, Long userId) {
        validateUser(userId);

        Chatroom chatroom = chatroomRepository.findById(id).orElse(null);
        if (chatroom == null || chatroom.isDeleted() || chatroom.getStatus().equals(ChatroomStatusEnum.DELETED)) {
            throw new NotFoundException(language.getString("chatroom.is_not_existed"));
        }
        isReadAllMessage(chatroom, userId);

        List<ChatroomUser> userList = chatroomUserRepository.findAllByChatroomId(id);
        if (cannotViewChatroom(userId, userList)) {
            throw new ForbiddenException();
        }
        return ChatroomRes.by(chatroom, true, userList);
    }

    private boolean cannotViewChatroom(Long userId, List<ChatroomUser> userList) {
        return Utils.isNullOrEmpty(userList)
            || userList.stream()
                    .noneMatch(t -> t.getUser().getId().equals(userId) &&
                            t.getStatus().equals(ChatroomUserStatusEnum.JOINING));
    }

    private void isReadAllMessage(Chatroom chatroom, Long userId) {
        if (Utils.isNullOrEmpty(chatroom.getMessages())) {
            return;
        }
        List<MessageTrackingStatus> unReadMessages = chatroom.getMessages().stream()
                .map(m -> m.getStatuses().stream()
                        .filter(s -> s.getUserId().equals(userId) && !s.isSeen())
                        .findFirst().orElse(null))
                .filter(s -> !Utils.isNullOrEmptyObject(s))
                .peek(s -> s.setSeen(true))
                .collect(Collectors.toList());
        if (!Utils.isNullOrEmpty(unReadMessages)) {
            messageTrackingStatusRepository.saveAll(unReadMessages);
        }
    }

    public MessageRes createMessage(AddingMessageReq req, Long userId) {
        User user = userRepository.getOne(userId);
        validateUser(user);

        req.normalize();

        Chatroom chatroom = chatroomRepository.findById(req.getChatroomId()).orElse(null);
        if (chatroom == null || chatroom.isDeleted() || chatroom.getStatus().equals(ChatroomStatusEnum.DELETED)) {
            throw new NotFoundException(language.getString("chatroom.is_not_existed"));
        }

        List<ChatroomUser> userList = chatroomUserRepository.findAllByChatroomId(req.getChatroomId());
        if (cannotViewChatroom(userId, userList)) {
            throw new ForbiddenException();
        }

        Message message = new Message(0L, req.getChatroomId(), req.getMessage());


        List<MessageTrackingStatus> statuses = new ArrayList<>();
        for (ChatroomUser cUser : userList) {
            statuses.add(new MessageTrackingStatus(0L, req.getChatroomId(), message, cUser.getUser().getId(), cUser.getUser().getId().equals(userId)));
        }
        message.setStatuses(statuses);
        messageRepository.save(message);
        return MessageRes.by(message);
    }

    public MessageRes isReadMessage(long id, Long userId) {
        validateUser(userId);

        Message message = messageRepository.findById(id).orElse(null);
        if (message == null) {
            throw new NotFoundException(language.getString("message.is_not_existed"));
        }

        List<MessageTrackingStatus> unReadMessages =
                messageTrackingStatusRepository.findByChatroomIdAndUserIdAndMessageIdLessThanEqualAndIsSeen(
                        message.getChatroomId(), userId, id, false);
        if (!Utils.isNullOrEmpty(unReadMessages)) {
            messageTrackingStatusRepository.saveAll(unReadMessages.stream()
                    .peek(s -> s.setSeen(true))
                    .collect(Collectors.toList()));
        }

        return MessageRes.by(message);
    }
}
