<template>
    <div class="home-page">
        <b-overlay :show="isLoading" rounded="sm" spinner-variant="primary">
            <section id="most-module">
                <MostModule
                        :modules="mostModules" />
            </section>
            <section id="post-list">
                <PostComponent
                        :posts="posts"
                        :currentUserId="currentUserId"
                        :currentUserName="currentUserName"
                        :currentUserAvatar="currentUserAvatar"
                        :currentUserColor="currentUserColor"
                        @createPost="createPost"
                        @updatePost="updatePost"
                        @deletePost="deletePost"
                        @createComment="createComment"
                        @showMoreComment="showMoreComment"
                        @showLessComment="showLessComment"
                        @showMoreRepliedComment="showMoreRepliedComment"
                        @showLessRepliedComment="showLessRepliedComment"
                        @likePost="likePost"
                        @dislikePost="dislikePost"
                        @replyComment="replyComment"
                        @editComment="editComment"
                        @deleteComment="deleteComment"
                        @editRepliedComment="editRepliedComment"
                        @deleteRepliedComment="deleteRepliedComment"
                />
                <div class="text-center mt-4">
                    <b-button v-show="!isLoading && !isLastPost"
                              type="submit" variant="primary"
                              @click="loadMore"
                    >
                        Load More
                    </b-button>
                </div>
            </section>
        </b-overlay>
    </div>
</template>

<script>
    import MostModule from "../components/home/MostModule";
    import PostComponent from "../components/home/Post";
    import {
        getList,
        createComment,
        like,
        dislike,
        createPost,
        updatePost,
        deletePost,
        replyComment, editComment, deleteComment
    } from "../services/post_service";
    import {POST, RESPONSE, VARIANT} from "../services/constants";
    import ToastHelper from "../helper/ToastHelper";
    export default {
        name: "Home",
        components: {PostComponent, MostModule},
        computed: {
            currentUserId: function () {
                return this.$store.getters.id
            },
            currentUserName: function () {
                return this.$store.getters.name
            },
            currentUserAvatar: function () {
                return this.$store.getters.avatar
            },
            currentUserColor: function () {
                return this.$store.getters.color
            },
            commentLimit: function () {
                return POST.COMMENT_PER_PAGE.LIST
            },
            repliedCommentLimit: function () {
                return POST.REPLIED_COMMENT_PER_PAGE.LIST
            }
        },
        data() {
            return {
                mostModules: [
                    [{
                        title: 'Module1',
                        icon: 'puzzle',
                        link: '#'
                    }, {
                        title: 'Module2',
                        icon: 'puzzle',
                        link: '#'
                    }, {
                        title: 'Module3',
                        icon: 'puzzle',
                        link: '#'
                    }, {
                      title: 'Module3-5',
                      icon: 'puzzle',
                      link: '#'
                    }], [{
                        title: 'Module4',
                        icon: 'puzzle',
                        link: '#'
                    }, {
                        title: 'Module5',
                        icon: 'puzzle',
                        link: '#'
                    }, {
                        title: 'Module6',
                        icon: 'puzzle',
                        link: '#'
                    }, {
                    title: 'Module6-5',
                    icon: 'puzzle',
                    link: '#'
                  }]
                ],
                posts: [],
                postReq: {
                    lastId: null,
                    keyword: null,
                    limit: 50
                },
                isLastPost: false,
                isLoading: false
            }
        },
        async mounted() {
            this.resetData()
            await this.getPosts()
        },
        methods: {
            resetData() {
                this.posts = []
                this.isLastPost = false
                this.postReq.lastId = null
            },
            async getPosts() {
                try {
                    this.isLoading = true
                    const res = await getList(this.postReq)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        if (res.data.responses && res.data.responses.length) {
                            res.data.responses.forEach(post => {
                                if (post.comments && post.comments.length) {
                                    post.commentIndex = Math.max(0, post.comments.length - this.commentLimit)
                                    post.comments.forEach(comment => {
                                        if (comment.repliedComments) {
                                            comment.repliedIndex = Math.max(0, comment.repliedComments.length - this.repliedCommentLimit)
                                        }
                                    })
                                }
                                this.posts.push(post)
                            })
                            this.postReq.lastId = res.data.responses[res.data.responses.length - 1].id
                            this.isLastPost = res.data.paging.number < res.data.paging.limit
                                || res.data.paging.total <= this.posts.length
                        } else {
                            this.isLastPost = true
                        }
                    } else {
                        console.log(res.message)
                    }
                } catch (e) {
                    console.log(e)
                } finally {
                    this.isLoading = false
                }
            },
            async createComment(postId, comment, index) {
                try {
                    this.isLoading = true
                    const res = await createComment(postId, {
                        message: comment
                    })
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.posts[index].comments.push({
                            id: res.data.newComment.id || 0,
                            message: comment,
                            user: {
                                id: this.currentUserId,
                                name: this.currentUserName,
                                color: this.currentUserColor,
                                avatar: this.currentUserAvatar
                            },
                            repliedComments: [],
                            createdAt: new Date()
                        })
                        this.showLessComment(index)
                    } else {
                        console.log(res.message)
                    }
                    this.posts[index].newComment = null
                } catch (e) {
                    console.log(e)
                } finally {
                    this.isLoading = false
                }
            },
            async loadMore() {
                await this.getPosts();
            },
            showMoreComment(index) {
                this.posts[index].commentIndex = Math.max(0, this.posts[index].commentIndex - this.commentLimit)
            },
            showLessComment(index) {
                this.posts[index].commentIndex = Math.max(0, this.posts[index].comments.length - this.commentLimit)
                if (this.posts[index].comments.length) {
                    this.posts[index].comments.forEach(comment => {
                        if (comment.repliedComments) {
                            comment.repliedIndex = Math.max(0, comment.repliedComments.length - this.repliedCommentLimit)
                        }
                    })
                }
            },
            showMoreRepliedComment(postIndex, commentIndex) {
                this.posts[postIndex].comments[commentIndex].repliedIndex = Math.max(0, this.posts[postIndex].comments[commentIndex].repliedIndex - this.repliedCommentLimit)
            },
            showLessRepliedComment(postIndex, commentIndex) {
                this.posts[postIndex].comments[commentIndex].repliedIndex = Math.max(0, this.posts[postIndex].comments[commentIndex].repliedComments.length - this.repliedCommentLimit)
            },
            async likePost(postId, index) {
                try {
                    this.isLoading = true
                    const res = await like(postId)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.posts[index].likedUsers.push({
                            id: this.currentUserId,
                            name: this.currentUserName,
                            color: this.currentUserColor,
                            avatar: this.currentUserAvatar
                        })
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch (e) {
                    console.log(e)
                } finally {
                    this.isLoading = false
                }
            },
            async dislikePost(postId, index) {
                try {
                    this.isLoading = true
                    const res = await dislike(postId)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        const deletedIndex = this.posts[index].likedUsers.findIndex(u => u.id === this.currentUserId)
                        if (deletedIndex > -1) {
                            this.posts[index].likedUsers.splice(deletedIndex, 1)
                        }
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch (e) {
                    console.log(e)
                } finally {
                    this.isLoading = false
                }
            },
            async createPost(title, content, shareMode) {
                try {
                    this.isLoading = true
                    const res = await createPost(title, content, shareMode)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.resetData()
                        this.getPosts()
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                        this.isLoading = false
                    }
                } catch (e) {
                    console.log(e)
                    this.isLoading = false
                }
            },
            async updatePost(id, title, content, shareMode) {
                try {
                    this.isLoading = true
                    const res = await updatePost(id, title, content, shareMode)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        ToastHelper.message(this.$t('post.message.update_success'))
                        this.resetData()
                        this.getPosts()
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                        this.isLoading = false
                    }
                } catch (e) {
                    console.log(e)
                    this.isLoading = false
                }
            },
            async deletePost(id) {
                try {
                    this.isLoading = true
                    const res = await deletePost(id)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        ToastHelper.message(this.$t('post.message.delete_success'))
                        this.resetData()
                        this.getPosts()
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                        this.isLoading = false
                    }
                } catch (e) {
                    console.log(e)
                    this.isLoading = false
                }
            },
            async replyComment(postId, postIndex, commentIndex, commentId, message) {
                try {
                    this.isLoading = true
                    const res = await replyComment(postId, commentId, message)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.posts[postIndex].comments[commentIndex].repliedComments.push({
                            id: res.data.comment.id || 0,
                            message: message,
                            user: {
                                id: this.currentUserId,
                                name: this.currentUserName,
                                color: this.currentUserColor,
                                avatar: this.currentUserAvatar
                            },
                            createdAt: new Date()
                        })
                        this.showLessRepliedComment(postIndex, commentIndex)
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch (e) {
                    console.log(e)
                } finally {
                    this.isLoading = false
                }
            },
            async editComment(postId, postIndex, commentIndex, commentId, message) {
                try {
                    this.isLoading = true
                    const res = await editComment(postId, commentId, message)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.posts[postIndex].comments[commentIndex].message = message
                        this.posts[postIndex].comments[commentIndex].status = POST.STATUS.MODIFIED
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch (e) {
                    console.log(e)
                } finally {
                    this.isLoading = false
                }
            },
            async deleteComment(postId, postIndex, commentIndex, commentId) {
                try {
                    this.isLoading = true
                    const res = await deleteComment(postId, commentId)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.posts[postIndex].comments.splice(commentIndex, 1)
                        this.showLessComment(postIndex)
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch (e) {
                    console.log(e)
                } finally {
                    this.isLoading = false
                }
            },
            async editRepliedComment(postId, postIndex, commentIndex, repliedCommentIndex, repliedCommentId, message) {
                try {
                    this.isLoading = true
                    const res = await editComment(postId, repliedCommentId, message)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.posts[postIndex].comments[commentIndex].repliedComments[repliedCommentIndex].message = message
                        this.posts[postIndex].comments[commentIndex].repliedComments[repliedCommentIndex].status = POST.STATUS.MODIFIED
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch (e) {
                    console.log(e)
                } finally {
                    this.isLoading = false
                }
            },
            async deleteRepliedComment(postId, postIndex, commentIndex, repliedCommentIndex, repliedCommentId) {
                try {
                    this.isLoading = true
                    const res = await deleteComment(postId, repliedCommentId)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.posts[postIndex].comments[commentIndex].repliedComments.splice(repliedCommentIndex, 1)
                        this.showLessRepliedComment(postIndex, commentIndex)
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch (e) {
                    console.log(e)
                } finally {
                    this.isLoading = false
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    #most-module {
        padding: 5rem 0.5rem 2rem;
        text-align: center;
    }
</style>