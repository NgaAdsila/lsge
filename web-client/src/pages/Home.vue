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
                        title: 'Module1'
                    }, {
                        title: 'Module2'
                    }, {
                        title: 'Module3'
                    }], [{
                        title: 'Module4'
                    }, {
                        title: 'Module5'
                    }, {
                        title: 'Module6'
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
        background: linear-gradient(rgba(255,255,255,.7), rgba(255,255,255,.7)), url(/card/my_card_wapper.jpg) no-repeat fixed top;
        background-size: cover;
        background-blend-mode: lighten;
        -ms-background-size: cover;
        -o-background-size: cover;
        -moz-background-size: cover;
        -webkit-background-size: cover;

        .shape {
            margin-top: 2.5rem;
        }
    }

    #most-module {
        padding: 5rem 0.5rem 2rem;
        text-align: center;
    }
</style>