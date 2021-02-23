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
                                <b-link v-show="post.commentIndex === 0 && post.comments.length > 5"
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
                                            {{ comment.message }}
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

    export default {
        name: "PostDetailComponent",
        components: {Avatar},
        props: [
            'post',
            'currentUserId',
            'currentUserName',
            'currentUserAvatar',
            'currentUserColor',
        ],
        methods: {
            formatTime(time) {
                return smartTime(time)
            },
            getIconShareMode(shareMode) {
                switch (shareMode) {
                    case POST.SHARE_MODE.PRIVATE:
                        return 'shield-lock-fill'
                    case POST.SHARE_MODE.PUBLIC:
                        return 'globe'
                    case POST.SHARE_MODE.FRIEND:
                        return 'people-fill'
                    default:
                        return ''
                }
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
                border-radius: 0.5rem;
                box-shadow: 0 0.15rem 0.15rem rgba(0, 0, 0, 0.15), inset 0 -1px 3px rgba(0, 0, 0, 0.15);

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
            }
        }
    }
}
</style>