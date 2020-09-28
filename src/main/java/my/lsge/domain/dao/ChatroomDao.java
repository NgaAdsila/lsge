package my.lsge.domain.dao;

import my.lsge.domain.entity.Chatroom;
import my.lsge.domain.enums.ChatroomTypeEnum;

import java.util.List;

public interface ChatroomDao {

    Chatroom findByTypeAndUsers(ChatroomTypeEnum type, List<Long> userIds);
}
