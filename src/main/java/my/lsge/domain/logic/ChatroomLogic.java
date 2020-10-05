package my.lsge.domain.logic;

import my.lsge.application.dto.chatroom.*;
import my.lsge.application.exception.ForbiddenException;
import my.lsge.application.exception.FormValidationException;
import my.lsge.application.exception.NotFoundException;
import my.lsge.domain.dao.ChatroomDao;
import my.lsge.domain.entity.*;
import my.lsge.domain.enums.ChatroomStatusEnum;
import my.lsge.domain.enums.ChatroomTypeEnum;
import my.lsge.domain.enums.ChatroomUserStatusEnum;
import my.lsge.domain.enums.MessageTypeEnum;
import my.lsge.domain.repository.ChatroomRepository;
import my.lsge.domain.repository.ChatroomUserRepository;
import my.lsge.domain.repository.MessageRepository;
import my.lsge.domain.repository.MessageTrackingStatusRepository;
import my.lsge.util.Utils;
import org.apache.commons.lang3.StringUtils;
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

        Message message = createNewMessage(req.getChatroomId(), req.getMessage(), userId, userList, MessageTypeEnum.NORMAL);
        chatroom.setLastMessage(message);
        chatroomRepository.save(chatroom);
        return MessageRes.by(message);
    }

    private Message createNewMessage(Long chatroomId, String messageText,
                                     Long userId, List<ChatroomUser> userList,
                                     MessageTypeEnum type) {
        Message message = new Message(0L, chatroomId, messageText, type);

        List<MessageTrackingStatus> statuses = new ArrayList<>();
        for (ChatroomUser cUser : userList) {
            statuses.add(new MessageTrackingStatus(
                    0L, chatroomId, message, cUser.getUser().getId(), cUser.getUser().getId().equals(userId)));
        }
        message.setStatuses(statuses);
        messageRepository.save(message);
        return message;
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

    public MessageRes update(long id, UpdatingChatroomReq req, Long userId) {
        User user = userRepository.getOne(userId);
        validateUser(user);

        Chatroom chatroom = chatroomRepository.findById(id).orElse(null);
        if (chatroom == null || chatroom.isDeleted() || chatroom.getStatus().equals(ChatroomStatusEnum.DELETED)) {
            throw new NotFoundException(language.getString("chatroom.is_not_existed"));
        }

        List<ChatroomUser> userList = chatroomUserRepository.findAllByChatroomId(id);
        if (cannotViewChatroom(userId, userList)) {
            throw new ForbiddenException();
        }
        String messageText = null;
        if (StringUtils.isNotBlank(chatroom.getName()) && StringUtils.isBlank(req.getName())) {
            messageText = String.format(language.getString("message.default.delete_name"), user.getName());
        } else if ((StringUtils.isNotBlank(req.getName()) && StringUtils.isBlank(chatroom.getName())) ||
                (StringUtils.isNotBlank(chatroom.getName()) && StringUtils.isNotBlank(req.getName())
                        && !chatroom.getName().equals(req.getName()))) {
            messageText = String.format(language.getString("message.default.update_name"),
                    user.getName(), req.getName());
        }

        chatroom.setName(req.getName());
        chatroomRepository.save(chatroom);

        if (StringUtils.isNotBlank(messageText)) {
            Message message = createNewMessage(id, messageText, userId, userList, MessageTypeEnum.DEFAULT);
            return MessageRes.by(message);
        }
        return null;
    }

    public MessageRes setNickname(long id, SettingNicknameReq req, Long userId) {
        User user = userRepository.getOne(userId);
        validateUser(user);

        Chatroom chatroom = chatroomRepository.findById(id).orElse(null);
        if (chatroom == null || chatroom.isDeleted() || chatroom.getStatus().equals(ChatroomStatusEnum.DELETED)) {
            throw new NotFoundException(language.getString("chatroom.is_not_existed"));
        }

        List<ChatroomUser> userList = chatroomUserRepository.findAllByChatroomId(id);
        if (cannotViewChatroom(userId, userList)) {
            throw new ForbiddenException();
        }
        List<String> messageTexts = new ArrayList<>();
        userList = userList.stream()
                .peek(u -> {
                    req.getUserList().stream()
                            .filter(t -> t.getId().equals(u.getUser().getId()))
                            .findFirst()
                            .ifPresent(t -> {
                                if (StringUtils.isBlank(t.getNickname()) && StringUtils.isNotBlank(u.getNickname())) {
                                    messageTexts.add(String.format(language.getString("message.default.set_nickname.delete"),
                                            u.getUser().getName()));
                                } else if (StringUtils.isNotBlank(t.getNickname()) && StringUtils.isBlank(u.getNickname())) {
                                    messageTexts.add(String.format(language.getString("message.default.set_nickname.add"),
                                            u.getUser().getName(), t.getNickname()));
                                } else if (StringUtils.isNotBlank(t.getNickname()) && StringUtils.isNotBlank(u.getNickname())
                                        && !t.getNickname().equals(u.getNickname())) {
                                    messageTexts.add(String.format(language.getString("message.default.set_nickname.update"),
                                            u.getUser().getName(), u.getNickname(), t.getNickname()));
                                }
                                u.setNickname(t.getNickname());
                            });
                })
                .collect(Collectors.toList());
        if (!Utils.isNullOrEmpty(messageTexts)) {
            chatroomUserRepository.saveAll(userList);
            String messageText = String.format(language.getString("message.default.set_nickname"),
                    user.getName(), "<br/>" + Utils.joinList(messageTexts, "<br/>"));
            Message message = createNewMessage(id, messageText, userId, userList, MessageTypeEnum.DEFAULT);
            return MessageRes.by(message);
        }
        return null;
    }
}
