<template>
    <div class="post-detail-page">
        <b-breadcrumb :items="crumbItems"></b-breadcrumb>
        <b-overlay :show="isLoading" rounded="sm" spinner-variant="primary">
            <PostDetailComponent
                    :post="post"
                    :currentUserId="currentUserId"
                    :currentUserName="currentUserName"
                    :currentUserAvatar="currentUserAvatar"
                    :currentUserColor="currentUserColor"
                    @showMoreComment="showMoreComment"
                    @showLessComment="showLessComment"
                    @createComment="createComment"
                    @likePost="likePost"
                    @dislikePost="dislikePost"
            />
        </b-overlay>
    </div>
</template>

<script>
    import { getById, createComment, like, dislike } from '../../services/post_service';
    import {RESPONSE, VARIANT} from "../../services/constants";
    import ToastHelper from "../../helper/ToastHelper";
    import PostDetailComponent from "../../components/post/PostDetailComponent";
    export default {
        name: "PostDetail",
        components: {PostDetailComponent},
        computed: {
            postId: function () {
                return this.$route.params.id
            },
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
            }
        },
        data() {
            return {
                post: {
                    user: {},
                    comments: [],
                    likedUsers: []
                },
                crumbItems: [
                    {
                        text: this.$t('common.label.home'),
                        to: { name: 'Home' }
                    },
                    {
                        text: this.$t('common.label.post_detail'),
                        active: true
                    }
                ],
                isLoading: true
            }
        },
        async mounted() {
            await this.getDetail()
        },
        methods: {
            async getDetail() {
                try {
                    this.isLoading = true
                    const res = await getById(this.postId)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.post = res.data
                        this.showLessComment()
                        this.isLoading = false
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                        setTimeout(async (self = this) => {
                            this.isLoading = false
                            await self.$router.push({ name: 'Home' })
                        }, 2000);
                    }
                } catch (e) {
                    this.isLoading = false
                    console.log(e)
                }
            },
            async createComment(comment) {
                try {
                    this.isLoading = true
                    const res = await createComment(this.postId, {
                        message: comment
                    })
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.post.newComment = null
                        this.post.comments.push({
                            id: 0,
                            message: comment,
                            user: {
                                id: this.currentUserId,
                                name: this.currentUserName,
                                color: this.currentUserColor,
                                avatar: this.currentUserAvatar
                            },
                            createdAt: new Date()
                        })
                        this.showLessComment()
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch (e) {
                    console.log(e)
                } finally {
                    this.isLoading = false
                }
            },
            showMoreComment() {
                this.post.commentIndex = Math.max(0, this.post.commentIndex - 5)
            },
            showLessComment() {
                this.post.commentIndex = Math.max(0, this.post.comments.length - 5)
            },
            async likePost() {
                try {
                    this.isLoading = true
                    const res = await like(this.postId)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.post.likedUsers.push({
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
            async dislikePost() {
                try {
                    this.isLoading = true
                    const res = await dislike(this.postId)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        const index = this.post.likedUsers.findIndex(u => u.id === this.currentUserId)
                        if (index > -1) {
                            this.post.likedUsers.splice(index, 1)
                        }
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

<style scoped>

</style>