<template>
    <div class="container post-component">
        <div class="post-create-new-post">
            <b-alert show variant="success" class="has-link" v-b-modal.create-post-modal>
                <Avatar :avatar="currentUserAvatar"
                        :color="currentUserColor"
                        :name="currentUserName"
                        :size="'sm'"
                        default-color="#28a745"
                />
                {{ $t('post.label.create_message') }}
            </b-alert>
        </div>
        <CreatePostModal @createPost="createPost" />

        <div v-for="(post, index) of posts" :key="'post-' + index" class="post-items mt-3 d-flex flex-row">
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
                    <span class="small time">
                        {{ formatTime(post.modifiedAt) }}<i>{{ isModified(post.status) ? ' (' + $t('post.label.modified') + ') ' : '' }}</i>
                    </span>
                    <span class="pl-1">
                        <b-icon
                                :icon="getIconShareMode(post.shareMode)"
                                variant="success"
                        ></b-icon>
                    </span>
                </div>
                <div class="post-title-content mt-1 position-relative">
                    <div class="post-title font-italic text-break">
                        <router-link :to="`/post/${post.id}`">{{ post.title }}</router-link>
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
                                        @click="dislikePost(post.id, index)"
                                        class="pr-1 has-link" variant="danger"></b-icon>
                                <b-icon v-show="!isLikedPost(post.likedUsers)"
                                        icon="heart"
                                        scale="1.2"
                                        @click="likePost(post.id, index)"
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
                                              @keypress.enter.exact="createComment(post, index)"
                                ></b-form-input>
                                <b-button class="position-absolute button-send-comment"
                                          type="submit" variant="light"
                                          @click="createComment(post, index)"
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
                                        @click="showMoreComment(index)">
                                    {{ $t('post.label.show_more_comment') }}
                                </b-link>
                                <b-link v-show="post.commentIndex === 0 && post.comments.length > 3"
                                        variant="success"
                                        @click="showLessComment(index)">
                                    {{ $t('post.label.show_less_comment') }}
                                </b-link>
                            </div>
                            <div v-for="(comment, i) of post.comments"
                                 v-show="i >= post.commentIndex"
                                 :key="'post-' + index + '-comment-' + i">
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
                                            {{ comment.message }}
                                        </div>
                                        <div class="post-comment-action">
                                            <span class="post-comment-action-item action-reply has-link"
                                                  @click="replyComment(post.id, comment.id)"
                                            >{{ $t('post.label.reply') }}</span>
                                            <span v-if="currentUserId === post.user.id"
                                                  class="post-comment-action-item action-edit has-link"
                                                  @click="editComment(post.id, comment.id)"
                                            >{{ $t('post.label.edit') }}</span>
                                            <span v-if="currentUserId === post.user.id"
                                                  class="post-comment-action-item action-delete has-link"
                                                  @click="deleteComment(post.id, comment.id)"
                                            >{{ $t('post.label.remove') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div v-if="post.user.id === currentUserId" class="post-item-actions position-absolute">
                        <b-icon icon="pencil"
                                class="mr-2 has-link"
                                v-b-tooltip.hover="$t('post.label.edit')"
                                v-b-modal="'edit-post-modal-' + post.id"
                                variant="primary"></b-icon>
                        <b-icon icon="trash"
                                class="has-link"
                                v-b-tooltip.hover="$t('post.label.remove')"
                                v-b-modal="'post-delete-' + post.id"
                                variant="danger"></b-icon>
                    </div>
                    <EditPostModal :post="post" @updatePost="updatePost" />
                    <ConfirmModal :id="'post-delete-' + post.id"
                                  :message="$t('post.message.delete_confirm')"
                                  @onOk="deletePost(post.id)" />
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import Avatar from "../common/Avatar";
    import { smartTime } from "../../utils/index";
    import {POST} from "../../services/constants";
    import CreatePostModal from "../modal/posts/CreatePostModal";
    import EditPostModal from "../modal/posts/EditPostModal";
    import ConfirmModal from "../modal/ConfirmModal";
    export default {
        name: "PostComponent",
        components: {ConfirmModal, EditPostModal, CreatePostModal, Avatar},
        props: [
            'posts',
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
            }
        },
        methods: {
            formatTime(time) {
                return smartTime(time)
            },
            createComment(post, index) {
                this.$emit('createComment', post.id, post.newComment, index)
            },
            showMoreComment(index) {
                this.$emit('showMoreComment', index)
                this.$forceUpdate()
            },
            showLessComment(index) {
                this.$emit('showLessComment', index)
                this.$forceUpdate()
            },
            isLikedPost(likedUsers) {
                return likedUsers.length > 0 && likedUsers.some(u => u.id === this.currentUserId)
            },
            likePost(id, index) {
                this.$emit('likePost', id, index)
                this.$forceUpdate()
            },
            dislikePost(id, index) {
                this.$emit('dislikePost', id, index)
                this.$forceUpdate()
            },
            createPost(title, content, shareMode) {
                this.$emit('createPost', title, content, shareMode);
            },
            updatePost(id, title, content, shareMode) {
                this.$emit('updatePost', id, title, content, shareMode)
            },
            deletePost(id) {
                this.$bvModal.hide('post-delete-' + id)
                this.$emit('deletePost', id)
            },
            replyComment(postId, commentId) {
                console.log('REPLY COMMENT: ', postId, commentId)
            },
            editComment(postId, commentId) {
                console.log('EDIT COMMENT: ', postId, commentId)
            },
            deleteComment(postId, commentId) {
                console.log('DELETE COMMENT: ', postId, commentId)
            },

        }
    }
</script>

<style lang="scss" scoped>
.post-component {
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
                border-radius: 0.5rem;
                box-shadow: 0 0.15rem 0.15rem rgba(0, 0, 0, 0.15);

                .post-item-actions {
                    top: 0;
                    right: 0;
                    padding: 0.5rem;
                    display: none;
                }

                &:hover {
                    box-shadow: 0 0.25rem 0.15rem rgba(0, 0, 0, 0.45);
                    .post-item-actions {
                        display: block;
                    }
                }

                .post-content {
                    max-width: 100%;
                    display: block;
                    display: -webkit-box;
                    -webkit-line-clamp: 5;
                    -webkit-box-orient: vertical;
                    overflow: hidden;
                    text-overflow: ellipsis;
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
            }
        }
    }
}
</style>