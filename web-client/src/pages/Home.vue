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
                        @createComment="createComment" />
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
    import { getList, createComment } from "../services/post_service";
    import {RESPONSE} from "../services/constants";
    export default {
        name: "Home",
        components: {PostComponent, MostModule},
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
                                id: this.$store.getters.id,
                                name: this.$store.getters.name,
                                color: this.$store.getters.color,
                                avatar: this.$store.getters.avatar
                            },
                            createdAt: new Date()
                        })
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