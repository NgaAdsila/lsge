<template>
    <div>
        <Header
                :user="user"
                @search="searchHeader"
                @logout="logout"/>
        <b-container fluid>
            <router-view />
        </b-container>
        <Footer />
        <ConfirmModal :message="confirmMessage" @onOk="onOk" />
    </div>
</template>

<script>
    import Header from "../components/Header";
    import Footer from "../components/Footer";
    import { signOut } from '../services/user_service';
    import ConfirmModal from "../components/modal/ConfirmModal";
    import {parseJwt} from "../utils";
    export default {
        name: "default",
        components: {ConfirmModal, Footer, Header},
        data() {
            return {
                user: {
                    name: ''
                },
                confirmMessage: '',
                refreshToken: undefined
            }
        },
        created() {
            this.user.name = this.$store.getters.name;
        },
        mounted() {
            this.refreshToken = setInterval(async (self = this) => {
                if (self.$store.getters.jwt && parseJwt(self.$store.getters.jwt).exp < Date.now() / 1000) {
                    await self.onOk();
                }
            }, 1000);
        },
        methods: {
            async logout() {
                this.confirmMessage = this.$t('logout.message.question');
                this.$bvModal.show('modal-confirm');
            },
            searchHeader(keyword = '') {
                if (!keyword || keyword === '') {
                    return;
                }
                //TODO: search all
                console.log('SEARCH HEADER: ', keyword);
            },
            async onOk() {
                this.$bvModal.hide('modal-confirm');
                await signOut();
                await this.$router.push({path: '/login'});
            }
        },
        beforeDestroy() {
            if (!this.$store.getters.isLogin) {
                clearInterval(this.refreshToken);
            }
        }
    }
</script>

<style scoped>

</style>