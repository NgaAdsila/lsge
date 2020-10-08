package my.lsge.domain.dao;

import my.lsge.application.dto.chatroom.ChatroomWithLastMessageDTO;
import my.lsge.domain.entity.Chatroom;
import my.lsge.domain.enums.ChatroomTypeEnum;

import java.util.List;

public interface ChatroomDao {

    Chatroom findByTypeAndUsers(ChatroomTypeEnum type, List<Long> userIds);

    List<ChatroomWithLastMessageDTO> findAllByTypeAndUser(ChatroomTypeEnum type, Long userId);
}
