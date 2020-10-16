<template>
  <div id="about-me-page">
    <b-breadcrumb :items="crumbItems"></b-breadcrumb>
    <section id="section-my-card">
      <MyCard
          :cardInfo="cardInfo" />
    </section>
  </div>
</template>

<script>
import MyCard from "../../components/about-me/MyCard";
export default {
  name: "index",
  components: {MyCard},
  data() {
    return {
      crumbItems: [
        {
          text: this.$t('common.label.home'),
          to: { name: 'Home' }
        },
        {
          text: this.$t('common.label.about_me'),
          active: true
        }
      ],
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
#about-me-page {
  #section-my-card {
    background: linear-gradient(rgba(255,255,255,.7), rgba(255,255,255,.7)), url(/card/my_card_wapper.jpg) no-repeat fixed top center;
    background-size: cover;
    background-blend-mode: lighten;
    -ms-background-size: cover;
    -o-background-size: cover;
    -moz-background-size: cover;
    -webkit-background-size: cover;
    padding: 2rem 0.5rem;
    min-height: 60vh;
  }
}
</style>