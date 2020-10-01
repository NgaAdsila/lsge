<template>
    <div class="home-page">
        <section id="section-my-card">
            <MyCard
                    :cardInfo="cardInfo" />
            <div class="text-center shape">
                <a href="" @click="toMostModule">
                    <b-iconstack>
                        <b-icon stacked icon="square"
                                variant="primary"
                                scale="2" rotate="45"></b-icon>
                        <b-icon icon="chevron-down"
                                animation="throb"></b-icon>
                    </b-iconstack>
                </a>
            </div>
        </section>
        <section id="most-module">
            <MostModule
                    :modules="mostModules" />
        </section>
    </div>
</template>

<script>
    import MyCard from "../components/home/MyCard";
    import MostModule from "../components/home/MostModule";
    export default {
        name: "Home",
        components: {MostModule, MyCard},
        data() {
            return {
                cardInfo: {
                    header: this.$t('home.my_card.header'),
                    contents: [
                        {
                            title: this.$t('home.my_card.content_title1'),
                            content: this.$t('home.my_card.content1')
                        },
                        {
                            title: this.$t('home.my_card.content_title2'),
                            content: this.$t('home.my_card.content2')
                        }
                    ],
                    sign: {
                        title: this.$t('home.my_card.sign_title'),
                        content: this.$t('home.my_card.sign')
                    },
                    footer: {
                        detail: {
                            label: this.$t('home.my_card.view_detail'),
                            link: '/about_me'
                        },
                        socials: [

                        ]
                    }
                },
                mostModules: [
                    [{
                        title: this.$t('common.label.chat_list'),
                        icon: 'chat-dots',
                        link: '/chat-list'
                    }, {
                        title: 'Module2',
                        icon: 'puzzle',
                        link: '#'
                    }, {
                        title: 'Module3',
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
                    }]
                ]
            }
        },
        methods: {
            toMostModule(event) {
                this.scrollToElement(event, document.getElementById("most-module").offsetTop - 35);
            },
            scrollToElement(event, elTop) {
                event.preventDefault();
                document.documentElement.scrollTo({
                    top: elTop,
                    behavior: 'smooth'
                });
                document.body.scrollTo({
                    top: elTop,
                    behavior: 'smooth'
                });
            }
        }
    }
</script>

<style lang="scss" scoped>
    #section-my-card {
        padding: 2rem 0.5rem;
        min-height: 60vh;

        .shape {
            margin-top: 2.5rem;
        }
    }

    #most-module {
        padding: 5rem 0.5rem 2rem;
        text-align: center;
    }
</style>