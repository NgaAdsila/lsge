<template>
    <div class="home-page">
        <section id="most-module">
            <MostModule
                    :modules="mostModules" />
        </section>
        <section id="post-list">
            <b-overlay :show="isLoading" rounded="sm" spinner-variant="primary">
                <PostComponent
                        :posts="posts"
                        :currentUserId="currentUserId"
                        :currentUserName="currentUserName"
                        :currentUserAvatar="currentUserAvatar"
                        :currentUserColor="currentUserColor"
                        @createComment="createComment"
                        @showMoreComment="showMoreComment"
                        @showLessComment="showLessComment"
                        @likePost="likePost"
                        @dislikePost="dislikePost" />
                <div class="text-center mt-4">
                    <b-button v-show="!isLastPost"
                              type="submit" variant="primary"
                              @click="loadMore"
                    >
                        Load More
                    </b-button>
                </div>
            </b-overlay>
        </section>
    </div>
</template>

<script>
    import MostModule from "../components/home/MostModule";
    import PostComponent from "../components/home/Post";
    import { getList, createComment, like, dislike } from "../services/post_service";
    import {RESPONSE, VARIANT} from "../services/constants";
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
            this.posts = []
            this.isLastPost = false
            this.postReq.lastId = null
            await this.getPosts();
        },
        methods: {
            async getPosts() {
                try {
                    this.isLoading = true
                    const res = await getList(this.postReq)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        if (res.data.responses && res.data.responses.length) {
                            res.data.responses.forEach(post => {
                                post.commentIndex = Math.max(0, post.comments.length - 3)
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
                    console.log('Comment: ', postId, comment)
                    const res = await createComment(postId, {
                        message: comment
                    })
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.posts[index].comments.push({
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
                this.posts[index].commentIndex = Math.max(0, this.posts[index].commentIndex - 3)
            },
            showLessComment(index) {
                this.posts[index].commentIndex = Math.max(0, this.posts[index].comments.length - 3)
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