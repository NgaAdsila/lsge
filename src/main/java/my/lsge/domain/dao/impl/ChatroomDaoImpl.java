package my.lsge.domain.dao.impl;

import my.lsge.domain.dao.ChatroomDao;
import my.lsge.domain.entity.Chatroom;
import my.lsge.domain.enums.ChatroomStatusEnum;
import my.lsge.domain.enums.ChatroomTypeEnum;
import my.lsge.domain.enums.ChatroomUserStatusEnum;
import my.lsge.util.Utils;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
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
}
