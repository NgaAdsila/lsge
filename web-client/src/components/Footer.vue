<template>
    <footer class="bd-footer docs-border">
        <b-container>
            <b-row class="justify-content-center">
                <b-col class="text-center text-sm-right col-sm order-sm-3 col-12">
                    <router-link to="/home">
                        <b-img src="../assets/logo.png" height="160" :alt="$t('common.label.slogan')"></b-img>
                    </router-link>
                </b-col>
                <b-col class="text-left col-md-4 col-auto">
                    <h5 class="bd-text-purple-bright mb-1">{{ $t('footer.label.documentation') }}</h5>
                    <ul class="list-unstyled ml-3">
                        <li v-for="(item, index) of documentList" :key="index">
                            <router-link :to="item.link" target="_self" class="footer-link">{{ item.label }}</router-link>
                        </li>
                    </ul>
                </b-col>
                <b-col class="text-left col-auto">
                    <h5 class="bd-text-purple-bright mb-1">{{ $t('footer.label.community') }}</h5>
                    <ul class="list-unstyled ml-3">
                        <li v-for="(item, index) of communityList" :key="index">
                            <a :href="item.link" target="_blank" class="footer-link">{{ item.label }}</a>
                        </li>
                    </ul>
                    <h5 class="bd-text-purple-bright mb-1">{{ $t('footer.label.release_note') }}</h5>
                    <ul class="list-unstyled ml-3">
                        <li v-for="(item, index) of noteList" :key="index">
                            <router-link :to="item.link" target="_blank" class="footer-link">{{ item.label }}</router-link>
                        </li>
                    </ul>
                </b-col>
            </b-row>
            <hr/>
            <p class="mb-2">{{ $t('footer.message.welcome') }}</p>
            <p class="mb-3">{{ $t('footer.message.copy_right', {version: 'v1.0', year: (new Date()).getFullYear()}) }}</p>
        </b-container>
    </footer>
</template>

<script>
    export default {
        name: "Footer",
        data() {
            return {
                documentList: [
                    {
                        label: this.$t('common.label.home'),
                        link: '/home'
                    },
                    {
                        label: this.$t('common.label.about_me'),
                        link: '#'
                    },
                    {
                        label: this.$t('common.label.contact_us'),
                        link: '#'
                    }
                ],
                communityList: [
                    {
                        label: 'Github',
                        link: 'https://github.com'
                    },
                    {
                        label: 'Facebook',
                        link: 'https://facebook.com'
                    },
                    {
                        label: 'Youtube',
                        link: 'https://youtube.com'
                    }
                ],
                noteList: [
                    {
                        label: 'Changelog',
                        link: '/'
                    }
                ]
            }
        }
    }
</script>

<style scoped>
    .docs-border {
        border-top: 3px solid #dee2e6;
    }
    .bd-footer {
        margin-top: 3rem;
        padding: 2rem 0;
        text-align: center;
        background-color: rgba(0,0,0,0.4);
        font-size: .875rem;
    }
    .bd-text-purple-bright {
        color: #7952b3;
    }
    .footer-link {
        color: #000000;
    }
    .footer-link:hover {
        color: dodgerblue;
    }
</style>