<template>
    <div class="home-page">
        <section id="most-module">
            <MostModule
                    :modules="mostModules" />
        </section>
        <section id="post-list">
            <PostComponent
                    :posts="posts"
                    @createComment="createComment" />
        </section>
    </div>
</template>

<script>
    import MostModule from "../components/home/MostModule";
    import PostComponent from "../components/home/Post";
    import { getList } from "../services/post_service";
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
                    keyword: null
                }
            }
        },
        async mounted() {
            await this.getPosts();
        },
        methods: {
            async getPosts() {
                try {
                    const res = await getList(this.postReq);
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.posts = res.data.responses;
                    } else {
                        console.log(res.message)
                    }
                } catch (e) {
                    console.log(e)
                }
            },
            async createComment(postId, comment) {
                try {
                    console.log('Comment: ', postId, comment)
                } catch (e) {
                    console.log(e)
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