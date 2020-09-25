<template>
    <div>
        <Header
                :user="user"
                @searchFriend="searchFriend"
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
    import { signOut, refreshToken } from '../services/user_service';
    import ConfirmModal from "../components/modal/ConfirmModal";
    import {parseJwt} from "../utils";
    import {RESPONSE} from "../services/constants";
    export default {
        name: "default",
        components: {ConfirmModal, Footer, Header},
        data() {
            return {
                user: {
                    name: this.$store.getters.name
                },
                confirmMessage: '',
                refreshToken: undefined
            }
        },
        mounted() {
            this.refreshToken = setInterval(async (self = this) => {
                if (self.$store.getters.jwt && parseJwt(self.$store.getters.jwt).exp < Date.now() / 1000) {
                    const res = await refreshToken();
                    if (res.status === RESPONSE.STATUS.ERROR) {
                        await self.logout();
                    }
                }
            }, 1000);
        },
        methods: {
            async logout() {
                this.confirmMessage = this.$t('logout.message.question');
                this.$bvModal.show('modal-confirm');
            },
            async searchFriend(keyword = '') {
                await this.$router.push({ name: 'FindFriend', query: { keyword: keyword.trim() } });
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