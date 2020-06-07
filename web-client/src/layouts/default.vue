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
    export default {
        name: "default",
        components: {ConfirmModal, Footer, Header},
        data() {
            return {
                user: {
                    name: ''
                },
                confirmMessage: ''
            }
        },
        created() {
            this.user.name = this.$store.getters.name;
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
        }
    }
</script>

<style scoped>

</style>