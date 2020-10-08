package my.lsge.domain.dao.impl;

import my.lsge.application.dto.chatroom.ChatroomWithLastMessageDTO;
import my.lsge.domain.dao.ChatroomDao;
import my.lsge.domain.entity.Chatroom;
import my.lsge.domain.enums.ChatroomStatusEnum;
import my.lsge.domain.enums.ChatroomTypeEnum;
import my.lsge.domain.enums.ChatroomUserStatusEnum;
import my.lsge.util.Utils;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ChatroomDaoImpl extends BaseDaoImpl<Chatroom> implements ChatroomDao {

    @Override
    public Chatroom findByTypeAndUsers(ChatroomTypeEnum type, List<Long> userIds) {
        try {
            if (type == null || Utils.isNullOrEmpty(userIds)) {
                return null;
            }
            StringBuilder query = new StringBuilder(String.format("SELECT cr FROM Chatroom cr " +
                    "WHERE cr.type = '%s' AND cr.status <> '%s' ", type, ChatroomStatusEnum.DELETED));
            for (long userId : userIds) {
                query.append(String.format(" AND EXISTS (SELECT 1 FROM ChatroomUser u " +
                        "WHERE u.chatroom = cr AND u.user.id = %s AND u.status <> '%s') ", userId, ChatroomUserStatusEnum.LEFT));
            }
            return entityManager.createQuery(query.toString(), Chatroom.class).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<ChatroomWithLastMessageDTO> findAllByTypeAndUser(ChatroomTypeEnum type, Long userId) {
        try {
            if (type == null || userId == null) {
                return new ArrayList<>();
            }
            String query = String.format(
                    "SELECT NEW my.lsge.application.dto.chatroom.ChatroomWithLastMessageDTO(c.id, c.lastMessage, u.user) " +
                    "FROM Chatroom c " +
                    "INNER JOIN ChatroomUser u ON c = u.chatroom AND u.user.id <> %s AND u.status <> '%s' " +
                    "WHERE c.type = '%s' AND c.status <> '%s' " +
                      "AND EXISTS (SELECT 1 FROM ChatroomUser u1 " +
                            "WHERE c = u1.chatroom AND u1.user.id = %s AND u1.status <> '%s')",
                    userId, ChatroomUserStatusEnum.LEFT, type,
                    ChatroomStatusEnum.DELETED, userId, ChatroomUserStatusEnum.LEFT);
            return entityManager.createQuery(query, ChatroomWithLastMessageDTO.class).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }
}
