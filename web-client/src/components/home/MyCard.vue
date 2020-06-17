<template>
    <b-row class="my-card-section">
        <b-col class="offset-xl-4 offset-lg-3 offset-md-2 offset-sm-1 my-information">
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
                <h3><b-icon icon="arrows-move" font-scale="0.75"
                            variant="success"
                            animation="spin"></b-icon> {{ content.title }}</h3>
                <div class="text-justify" v-html="content.content"></div>
            </div>
            <div class="my-card-sign">
                <span>{{ cardInfo.sign.title }}</span>
                <p class="text-color-gray">{{ cardInfo.sign.content }}</p>
            </div>
            <div class="my-card-footer">
                <b-button variant="primary" @click="cardInfo.footer.detail.link">
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
    .my-card-section {
        .my-information {
            font-family: "Times New Roman",sans-serif;

            .my-card-content {
                padding: 1rem 0;
                .text-justify {
                    padding: 0 1rem;
                }
            }
        }

        .my-card-sign p {
            font-family: "Courier New", sans-serif !important;
        }
    }
</style>