<template>
    <div class="container post-detail-component">
        <div class="post-items mt-3 d-flex flex-row">
            <div class="post-avatar pr-2">
                <Avatar
                        :avatar="post.user.avatar"
                        :color="post.user.color"
                        :name="post.user.name"
                        default-color="#28a745"
                />
            </div>
            <div class="post-details">
                <div class="post-username font-weight-bold">
                    {{ post.user.name }}
                </div>
                <div class="post-time-status pr-2">
                    <span class="small time">{{ formatTime(post.modifiedAt) }}</span>
                    <span class="pl-1">
                        <b-icon
                                :icon="getIconShareMode(post.shareMode)"
                                variant="success"
                        ></b-icon>
                    </span>
                </div>
                <div class="post-title-content mt-1">
                    <div class="post-title font-italic text-break">
                        {{ post.title }}
                    </div>
                    <div class="post-content text-break text-pre">
                        {{ post.content }}
                    </div>
                    <div class="post-react-comments">
                        <div class="post-react-comment-icon d-flex flex-row">
                            <div class="post-reaction-icon pr-3">
                                <b-icon v-show="isLikedPost(post.likedUsers)"
                                        icon="heart-fill"
                                        scale="1.2"
                                        @click="dislikePost"
                                        class="pr-1 has-link" variant="danger"></b-icon>
                                <b-icon v-show="!isLikedPost(post.likedUsers)"
                                        icon="heart"
                                        scale="1.2"
                                        @click="likePost"
                                        class="pr-1 has-link" variant="danger"></b-icon>
                                <span class="small">{{ post.likedUsers.length }}</span>
                            </div>
                            <div class="post-comment-icon">
                                <b-icon :icon="post.comments.length ? 'chat-square-dots-fill' : 'chat-square-dots'"
                                        scale="1.2"
                                        class="pr-1" variant="primary"></b-icon>
                                <span class="small">{{ post.comments.length }}</span>
                            </div>
                        </div>
                        <div class="post-comment-input d-flex flex-row mt-1">
                            <div class="post-comment-input-avatar pr-2">
                                <Avatar :avatar="currentUserAvatar"
                                        :color="currentUserColor"
                                        :name="currentUserName"
                                        default-color="#28a745"
                                />
                            </div>
                            <div class="post-comment-input-form position-relative">
                                <b-form-input class="post-comment-input-form-tag"
                                              v-model="post.newComment"
                                              required
                                              type="text"
                                              :placeholder="$t('common.label.comment')"
                                              max="2000"
                                              @keypress.enter.exact="createComment()"
                                ></b-form-input>
                                <b-button class="position-absolute button-send-comment"
                                          type="submit" variant="light"
                                          @click="createComment()"
                                          :disabled="!post.newComment || post.newComment.trim() === ''"
                                >
                                    <b-icon icon="cursor-fill" rotate="45"
                                            :variant="!post.newComment || post.newComment.trim() === '' ? 'info' : 'primary'"
                                            scale="1.25"></b-icon>
                                </b-button>
                            </div>
                        </div>
                        <div v-if="post.comments.length" class="post-comment-list container mt-1">
                            <div class="button-show-comment mt-1 mb-1 small">
                                <b-link v-show="post.commentIndex > 0"
                                          variant="success"
                                          @click="showMoreComment">
                                    {{ $t('post.label.show_more_comment') }}
                                </b-link>
                                <b-link v-show="post.commentIndex === 0 && post.comments.length > commentLimit"
                                          variant="success"
                                          @click="showLessComment">
                                    {{ $t('post.label.show_less_comment') }}
                                </b-link>
                            </div>
                            <div v-for="(comment, i) of post.comments"
                                 v-show="i >= post.commentIndex"
                                 :key="'post-comment-' + i">
                                <div class="post-comment-item d-flex flex-row mb-1">
                                    <div class="post-comment-avatar pr-2">
                                        <Avatar
                                                :avatar="comment.user.avatar"
                                                :color="comment.user.color"
                                                :name="comment.user.name"
                                                :size="'sm'"
                                                default-color="#28a745"
                                        />
                                    </div>
                                    <div class="post-comment-details small">
                                        <div class="post-comment-header d-flex flex-row justify-content-between">
                                            <div class="post-comment-username font-weight-bold">
                                                {{ comment.user.name }}
                                            </div>
                                            <div class="post-comment-time">
                                                {{ formatTime(comment.createdAt) }}
                                            </div>
                                        </div>
                                        <div class="post-comment-content text-break">
                                            {{ comment.message }} <i class="small">{{ isModified(comment.status) ? ' (' + $t('post.label.modified') + ') ' : '' }}</i>
                                        </div>
                                        <div class="post-comment-action">
                                            <span class="post-comment-action-item action-reply has-link"
                                                  :class="{ 'disabled-link': commentId === comment.id && action === COMMENT_ACTIONS.REPLY }"
                                                  @click="replyComment(comment.id)"
                                            >{{ $t('post.label.reply') }}</span>
                                            <span v-if="currentUserId === comment.user.id"
                                                  class="post-comment-action-item action-edit has-link"
                                                  :class="{ 'disabled-link': commentId === comment.id && action === COMMENT_ACTIONS.EDIT }"
                                                  @click="editComment(comment)"
                                            >{{ $t('post.label.edit') }}</span>
                                            <span v-if="currentUserId === comment.user.id"
                                                  class="post-comment-action-item action-delete has-link"
                                                  :class="{ 'disabled-link': commentId === comment.id && action === COMMENT_ACTIONS.DELETE }"
                                                  @click="deleteComment(comment.id)"
                                            >{{ $t('post.label.remove') }}</span>
                                        </div>
                                        <div class="post-comment-action-form">
                                            <div v-if="commentId === comment.id && action === COMMENT_ACTIONS.REPLY">
                                                <b-form-input class="post-action-comment-input"
                                                              v-model="commentActionMessage"
                                                              required
                                                              type="text"
                                                              max="2000"
                                                              @keypress.enter.exact="submitReplyComment(i)"
                                                ></b-form-input>
                                                <b-button class="btn btn-success button-action-comment-submit"
                                                          type="submit"
                                                          @click="submitReplyComment(i)"
                                                          :disabled="!commentActionMessage || commentActionMessage.trim() === ''"
                                                >{{ $t('post.label.reply') }}</b-button>
                                                <b-button class="btn btn-secondary button-action-comment-submit"
                                                          @click="resetActionComment"
                                                >{{ $t('common.label.cancel') }}</b-button>
                                            </div>
                                            <div v-if="commentId === comment.id && action === COMMENT_ACTIONS.EDIT">
                                                <b-form-input class="post-action-comment-input"
                                                              v-model="commentActionMessage"
                                                              required
                                                              type="text"
                                                              max="2000"
                                                              @keypress.enter.exact="submitEditComment(i)"
                                                ></b-form-input>
                                                <b-button class="btn btn-success button-action-comment-submit"
                                                          type="submit"
                                                          @click="submitEditComment(i)"
                                                          :disabled="!commentActionMessage || commentActionMessage.trim() === ''"
                                                >{{ $t('post.label.edit') }}</b-button>
                                                <b-button class="btn btn-secondary button-action-comment-submit"
                                                          @click="resetActionComment"
                                                >{{ $t('common.label.cancel') }}</b-button>
                                            </div>
                                            <ConfirmModal :id="`post-comment-delete-${comment.id}`"
                                                          :message="$t('post.message.delete_comment_confirm')"
                                                          @onOk="submitDeleteComment(i)"
                                                          @hide="resetActionComment" />
                                        </div>
                                        <div v-if="comment.repliedComments && comment.repliedComments.length"
                                             class="post-comment-replied-list mt-1">
                                            <div class="button-show-comment mt-1 mb-1">
                                                <b-link v-show="comment.repliedIndex > 0"
                                                        variant="success"
                                                        @click="showMoreRepliedComment(i)">
                                                    {{ $t('post.label.show_more_reply') }}
                                                </b-link>
                                                <b-link v-show="comment.repliedIndex === 0 && comment.repliedComments.length > repliedCommentLimit"
                                                        variant="success"
                                                        @click="showLessRepliedComment(i)">
                                                    {{ $t('post.label.show_less_reply') }}
                                                </b-link>
                                            </div>
                                            <div v-for="(repliedComment, ri) of comment.repliedComments"
                                                 v-show="ri >= comment.repliedIndex"
                                                 :key="'post-comment-' + i + '-replied-' + ri">
                                                <div class="post-comment-item d-flex flex-row mb-1">
                                                    <div class="post-comment-avatar pr-2">
                                                        <Avatar
                                                                :avatar="repliedComment.user.avatar"
                                                                :color="repliedComment.user.color"
                                                                :name="repliedComment.user.name"
                                                                :size="'sm'"
                                                                default-color="#28a745"
                                                        />
                                                    </div>
                                                    <div class="post-comment-details">
                                                        <div class="post-comment-header d-flex flex-row justify-content-between">
                                                            <div class="post-comment-username font-weight-bold">
                                                                {{ repliedComment.user.name }}
                                                            </div>
                                                            <div class="post-comment-time small">
                                                                {{ $t('common.label.reply_at') + formatTime(repliedComment.createdAt) }}
                                                            </div>
                                                        </div>
                                                        <div class="post-comment-content text-break">
                                                            {{ repliedComment.message }} <i class="small">{{ isModified(repliedComment.status) ? ' (' + $t('post.label.modified') + ') ' : '' }}</i>
                                                        </div>
                                                        <div class="post-comment-action">
                                                            <span v-if="currentUserId === repliedComment.user.id"
                                                                  class="post-comment-action-item action-edit has-link"
                                                                  :class="{ 'disabled-link': commentId === comment.id && repliedCommentId === repliedComment.id && action === COMMENT_ACTIONS.REPLIED_EDIT }"
                                                                  @click="editRepliedComment(comment.id, repliedComment)"
                                                            >{{ $t('post.label.edit') }}</span>
                                                            <span v-if="currentUserId === repliedComment.user.id"
                                                                  class="post-comment-action-item action-delete has-link"
                                                                  :class="{ 'disabled-link': commentId === comment.id && repliedCommentId === repliedComment.id && action === COMMENT_ACTIONS.REPLIED_DELETE }"
                                                                  @click="deleteRepliedComment(comment.id, repliedComment.id)"
                                                            >{{ $t('post.label.remove') }}</span>
                                                        </div>
                                                        <div class="post-comment-action-form">
                                                            <div v-if="commentId === comment.id && repliedCommentId === repliedComment.id && action === COMMENT_ACTIONS.REPLIED_EDIT">
                                                                <b-form-input class="post-action-comment-input"
                                                                              v-model="commentActionMessage"
                                                                              required
                                                                              type="text"
                                                                              max="2000"
                                                                              @keypress.enter.exact="submitEditRepliedComment(i, ri)"
                                                                ></b-form-input>
                                                                <b-button class="btn btn-success button-action-comment-submit"
                                                                          type="submit"
                                                                          @click="submitEditRepliedComment(i, ri)"
                                                                          :disabled="!commentActionMessage || commentActionMessage.trim() === ''"
                                                                >{{ $t('post.label.edit') }}</b-button>
                                                                <b-button class="btn btn-secondary button-action-comment-submit"
                                                                          @click="resetActionComment"
                                                                >{{ $t('common.label.cancel') }}</b-button>
                                                            </div>
                                                            <ConfirmModal :id="`post-comment-delete-${comment.id}-${repliedComment.id}`"
                                                                          :message="$t('post.message.delete_reply_confirm')"
                                                                          @onOk="submitDeleteRepliedComment(i, ri)"
                                                                          @hide="resetActionComment" />
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import Avatar from "../common/Avatar";
    import {POST} from "../../services/constants";
    import {smartTime} from "../../utils";
    import ConfirmModal from "../modal/ConfirmModal";

    export default {
        name: "PostDetailComponent",
        components: {Avatar, ConfirmModal},
        props: [
            'post',
            'currentUserId',
            'currentUserName',
            'currentUserAvatar',
            'currentUserColor',
        ],
        computed: {
            getIconShareMode() {
                return shareMode => POST.SHARE_MODE_ICON[shareMode]
            },
            isModified() {
                return status => POST.STATUS.MODIFIED === status
            },
            commentLimit: function () {
                return POST.COMMENT_PER_PAGE.DETAIL
            },
            repliedCommentLimit: function () {
                return POST.REPLIED_COMMENT_PER_PAGE.DETAIL
            }
        },
        data() {
            return {
                repliedCommentId: null,
                commentId: null,
                action: null,
                commentActionMessage: null,
                COMMENT_ACTIONS: POST.COMMENT_ACTIONS
            }
        },
        methods: {
            formatTime(time) {
                return smartTime(time)
            },
            createComment() {
                this.$emit('createComment', this.post.newComment)
            },
            showMoreComment() {
                this.$emit('showMoreComment')
                this.$forceUpdate()
            },
            showLessComment() {
                this.$emit('showLessComment')
                this.$forceUpdate()
            },
            showMoreRepliedComment(index) {
                this.$emit('showMoreRepliedComment', index)
                this.$forceUpdate()
            },
            showLessRepliedComment(index) {
                this.$emit('showLessRepliedComment', index)
                this.$forceUpdate()
            },
            isLikedPost(likedUsers) {
                return likedUsers.length > 0 && likedUsers.some(u => u.id === this.currentUserId)
            },
            likePost() {
                this.$emit('likePost')
                this.$forceUpdate()
            },
            dislikePost() {
                this.$emit('dislikePost')
                this.$forceUpdate()
            },
            replyComment(commentId) {
                this.commentId = commentId
                this.action = POST.COMMENT_ACTIONS.REPLY
                this.commentActionMessage = null
            },
            editComment(comment) {
                this.commentId = comment.id
                this.action = POST.COMMENT_ACTIONS.EDIT
                this.commentActionMessage = comment.message
            },
            deleteComment(commentId) {
                this.commentId = commentId
                this.action = POST.COMMENT_ACTIONS.DELETE
                this.commentActionMessage = null
                this.$bvModal.show(`post-comment-delete-${commentId}`)
            },
            submitReplyComment(commentIndex) {
                this.$emit('replyComment', this.post.id, commentIndex, this.commentId, this.commentActionMessage)
                this.resetActionComment()
            },
            submitEditComment(commentIndex) {
                this.$emit('editComment', this.post.id, commentIndex, this.commentId, this.commentActionMessage)
                this.resetActionComment()
            },
            resetActionComment() {
                this.commentId = null
                this.repliedCommentId = null
                this.action = null
                this.commentActionMessage = null
            },
            submitDeleteComment(commentIndex) {
                this.$bvModal.hide(`post-comment-delete-${this.commentId}`)
                this.$emit('deleteComment', this.post.id, commentIndex, this.commentId)
                this.resetActionComment()
            },
            editRepliedComment(commentId, repliedComment) {
                this.commentId = commentId
                this.repliedCommentId = repliedComment.id
                this.action = POST.COMMENT_ACTIONS.REPLIED_EDIT
                this.commentActionMessage = repliedComment.message
            },
            deleteRepliedComment(commentId, repliedCommentId) {
                this.commentId = commentId
                this.repliedCommentId = repliedCommentId
                this.action = POST.COMMENT_ACTIONS.REPLIED_DELETE
                this.commentActionMessage = null
                this.$bvModal.show(`post-comment-delete-${commentId}-${repliedCommentId}`)
            },
            submitEditRepliedComment(commentIndex, repliedCommentIndex) {
                this.$emit('editRepliedComment', this.post.id, commentIndex, repliedCommentIndex, this.repliedCommentId, this.commentActionMessage)
                this.resetActionComment()
            },
            submitDeleteRepliedComment(commentIndex, repliedCommentIndex) {
                this.$bvModal.hide(`post-comment-delete-${this.commentId}-${this.repliedCommentId}`)
                this.$emit('deleteRepliedComment', this.post.id, commentIndex, repliedCommentIndex, this.repliedCommentId)
                this.resetActionComment()
            }
        }
    }
</script>

<style lang="scss" scoped>
.post-detail-component {
    .post-items {
        .post-details {
            width: calc(100% - 5rem);

            .post-time-status {
                .time {
                    opacity: 0.8;
                }
            }

            .post-title-content {
                width: 100%;
                text-align: justify;
                padding: 0.5rem;

                .post-title {
                    color: #279dff;
                }

                .post-react-comments {
                    .post-comment-input {
                        width: 100%;
                        .post-comment-input-form {
                            width: 100%;

                            .post-comment-input-form-tag {
                                height: 3rem;
                                padding-right: 3rem;
                                font-size: 95%;
                            }

                            .button-send-comment {
                                right: 0.75rem;
                                top: 0.75rem;
                                padding: 0.1rem 0.25rem;
                                background-color: transparent;
                                border-color: transparent;
                                &:hover {
                                    background-color: transparent;
                                    border-color: transparent;
                                    opacity: 0.7;
                                }
                            }
                        }
                    }
                    .post-comment-list {
                        .button-show-comment {

                        }
                        .post-comment-details {
                            width: calc(100% - 2rem);
                        }
                    }
                }
                .post-comment-action {
                    color: #007bff;
                    font-size: 90%;
                    font-weight: bold;
                    .post-comment-action-item {
                        margin-right: 0.5rem;

                        &.action-delete {
                            color: red;
                        }
                    }
                }
                .post-comment-action-form {
                    .post-action-comment-input {
                        height: 1.75rem;
                        line-height: 1.75rem;
                        font-size: 0.85rem;
                        padding: 0.25rem;
                    }
                    .button-action-comment-submit {
                        font-size: 0.75rem ;
                        padding: 0.15rem;
                        margin-top: 0.25rem;
                        margin-right: 0.25rem;
                    }
                }
            }
        }
    }
}
</style>