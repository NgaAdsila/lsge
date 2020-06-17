<template>
    <MyCard
            :cardInfo="cardInfo" />
</template>

<script>
    import MyCard from "../components/home/MyCard";
    export default {
        name: "Home",
        components: {MyCard},
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
                }
            }
        }
    }
</script>

<style scoped>

</style>