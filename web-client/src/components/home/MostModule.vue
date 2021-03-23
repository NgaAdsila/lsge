<template>
    <div class="most-module-section">
        <div class="text-center mb-4 text-uppercase font-weight-bold">
          {{ $t('home.most_module.title') }}
        </div>
        <div class="most-module-title">
            <b-iconstack>
                <b-icon stacked icon="square-fill"
                        scale="3"
                        rotate="45" variant="success"></b-icon>
                <b-icon stacked icon="union" scale="1.2" animation="throb"></b-icon>
            </b-iconstack>
        </div>
        <div class="most-module-content">
            <b-carousel controls>
                <b-carousel-slide v-for="(items, i) of modules" :key="'items-' + i">
                    <template v-slot:img>
                        <b-row>
                            <b-col v-for="(item, j) of items"
                                   :key="'item-' + i + j"
                                   class="most-module-col col-12"
                                   :class="'col-md-' + (12 / items.length)">
                                <div class="most-module-item" @click="$router.push({ path: item.link })">
                                    <b-icon :icon="item.icon"></b-icon> {{ item.title }}
                                </div>
                            </b-col>
                        </b-row>
                    </template>
                </b-carousel-slide>
            </b-carousel>
        </div>
    </div>
</template>

<script>
    export default {
        name: "MostModule",
        props: [
            'modules'
        ]
    }
</script>

<style lang="scss" scoped>
.most-module-content {
    margin-top: 3rem;

    .most-module-col:first-child {
        @media(min-width: 768px) {
            text-align: right;
        }
    }

    .most-module-col:last-child {
        @media(min-width: 768px) {
            text-align: left;
        }
    }

    .most-module-item {
        width: 13rem;
        height: 13rem;
        line-height: 13rem;
        display: inline-block;
        border: 1px solid #dbdbdb;
        border-radius: 30% 10%;
        text-align: center;
        margin: 0.5rem 0;
        &:hover {
            cursor: pointer;
            font-weight: bold;
        }
    }
}
</style>