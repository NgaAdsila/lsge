package my.lsge.domain.logic;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import my.lsge.application.dto.post.*;
import my.lsge.application.exception.ForbiddenException;
import my.lsge.application.exception.NotFoundException;
import my.lsge.domain.dao.PostDao;
import my.lsge.domain.entity.*;
import my.lsge.domain.enums.*;
import my.lsge.domain.repository.CommentRepository;
import my.lsge.domain.repository.PostRepository;
import my.lsge.domain.repository.RelationshipRepository;
import my.lsge.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PostLogic extends BaseLogic {

    @Autowired
    private PostDao postDao;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MapperFacade mapper;

    public PostFilterRes filter(PostFilterReq req, Long userId) {
        validateUser(userId);

        req.normalize();
        PostFilterRes res = new PostFilterRes();
        List<Long> friendIds = getFriendIds(userId);
        long count = postDao.filterCount(req, friendIds, userId);
        if (count > 0) {
            res.setCount(count);
            List<Post> posts = postDao.filter(req, friendIds, userId);
            if (!Utils.isNullOrEmpty(posts)) {
                List<Long> postIds = posts.stream().map(Post::getId).collect(Collectors.toList());
                List<Comment> comments = commentRepository.findByReferenceTypeAndReferenceIdIn(CommentReferenceTypeEnum.POST, postIds);
                List<User> userList = userRepository.findAll();

                List<Post> rootList = getRootPostList(posts);
                res.setResponses(posts.stream()
                        .map(p -> PostFilterItemRes.by(p, comments, userList, rootList))
                        .collect(Collectors.toList()));
            }
        }
        res.setPaging(req);
        return res;
    }

    private List<Post> getRootPostList(List<Post> posts) {
        List<Long> rootIds = posts.stream()
                .filter(p -> p.getRootId() != null)
                .map(Post::getRootId)
                .collect(Collectors.toList());
        if (Utils.isNullOrEmpty(rootIds)) {
            return new ArrayList<>();
        }
        return postRepository.findByIdIn(rootIds);
    }

    private List<Long> getFriendIds(Long userId) {
        List<Relationship> relationships = relationshipRepository.findFriends(RelationShipStatusEnum.APPROVED, userId);
        List<Long> friendIds = new ArrayList<>();
        if (!Utils.isNullOrEmpty(relationships)) {
             friendIds.addAll(relationships.stream().map(r -> {
                 if (r.getId().getReqUserId().equals(userId)) {
                     return r.getId().getRecUserId();
                 }
                 return r.getId().getReqUserId();
             }).collect(Collectors.toSet()));
        }
        return friendIds;
    }

    public PostRes add(AddingPostReq req, Long userId) {
        validateUser(userId);

        Post post = new Post(0L, req.getTitle(), req.getContent(), req.isHasImage(),
                req.getShareMode(), PostStatusEnum.CREATED, null, null);
        postRepository.save(post);
        return PostRes.by(post);
    }

    public PostRes update(UpdatingPostReq req, Long userId) {
        validateUser(userId);

        Post post = postRepository.findById(req.getId()).orElse(null);
        if (post == null || post.isDeleted()) {
            throw new NotFoundException(language.getString("post.is_not_existed"));
        }
        if (!userId.equals(post.getCreatedBy())) {
            throw new ForbiddenException();
        }
        req.normalize();
        mapUpdatingReqToEntity(post, req);
        postRepository.save(post);
        return PostRes.by(post);
    }

    private void mapUpdatingReqToEntity(Post post, UpdatingPostReq req) {
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post.setShareMode(req.getShareMode());
        if (post.getRootId() != null) {
            post.setShareTitle(req.getShareTitle());
        }
        post.setStatus(PostStatusEnum.MODIFIED);
    }

    public PostRes addComment(Long postId, AddingCommentReq req, Long userId) {
        validateUser(userId);

        Post post = postRepository.findById(postId).orElse(null);
        if (post == null || post.isDeleted() || !hasViewRole(post, userId)) {
            throw new NotFoundException(language.getString("post.is_not_existed"));
        }
        Comment comment = new Comment(
                0L, CommentReferenceTypeEnum.POST, postId,
                req.getMessage(), 0L, CommentStatusEnum.CREATED);
        commentRepository.save(comment);
        return PostRes.by(post);
    }

    private boolean hasViewRole(Post post, long userId) {
        if (post.getShareMode().equals(PostShareModeEnum.PUBLIC) || post.getCreatedBy().equals(userId)) {
            return true;
        }
        if (post.getShareMode().equals(PostShareModeEnum.PRIVATE)) {
            return post.getCreatedBy().equals(userId);
        }
        Relationship relationship = relationshipRepository
                .findByUserIdsAndStatus(RelationShipStatusEnum.APPROVED, userId, post.getCreatedBy())
                .orElse(null);
        return relationship != null;
    }
}
