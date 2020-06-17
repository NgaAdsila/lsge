<template>
    <b-row class="home-page">
        <b-col class="col-md-5 text-right my-photo">
            <b-img src="/card/my_photo.jpg" :alt="$t('common.label.slogan')" />
        </b-col>
        <b-col class="col-md-7 my-information">
            <div class="my-card-name">
                <h1 class="text-uppercase font-weight-bold">
                    {{ cardInfo.header }}
                </h1>
                <h2 class="text-uppercase text-color-gray">
                    {{ $t('common.label.site_name') }}
                </h2>
            </div>
            <div v-for="(content, index) of cardInfo.contents"
                 :key="'content_' + index"
                 class="my-card-content">
                <h3>{{ content.title }}</h3>
                <span class="text-justify text-color-gray" v-html="content.content"></span>
            </div>
            <div class="my-card-sign">
                <span>{{ cardInfo.sign.title }}</span>
                <p class="text-color-gray">{{ cardInfo.sign.content }}</p>
            </div>
            <div class="my-card-footer">
                <b-button variant="outline-primary" @click="cardInfo.footer.detail.link">
                    {{ cardInfo.footer.detail.label }}
                </b-button>
            </div>
        </b-col>
    </b-row>
</template>

<script>
    export default {
        name: "MyCard",
        props: [
            'cardInfo'
        ]
    }
</script>

<style lang="scss" scoped>
    .home-page {
        margin: 10% auto;
        .my-photo {
            @media(max-width: 768px) {
                display: none;
            }

            img {
                border-radius: 1rem;
                max-width: 90%;
                max-height: 37.5rem;
                box-shadow: 0.5rem 0.5rem 0.5rem lightgray;

                @media (max-width: 768px) {
                    max-width: 100% !important;
                }
            }
        }
        .my-information {
            font-family: "Times New Roman",sans-serif;

            .my-card-content {
                padding: 1rem 1rem;
            }

        }

        .my-card-sign p {
            font-family: "fantasy", sans-serif !important;
        }
    }
</style>