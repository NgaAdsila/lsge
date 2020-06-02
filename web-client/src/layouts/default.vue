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
    </div>
</template>

<script>
    import Header from "../components/Header";
    import Footer from "../components/Footer";
    import { signOut } from '../services/user_service';
    export default {
        name: "default",
        components: {Footer, Header},
        data() {
            return {
                user: {
                    name: ''
                }
            }
        },
        created() {
            this.user.name = this.$store.getters.name;
        },
        methods: {
            async logout() {
                await signOut();
                this.$router.push({path: '/login'});
            },
            searchHeader(keyword = '') {
                if (!keyword || keyword === '') {
                    return;
                }
                //TODO: search all
                console.log('SEARCH HEADER: ', keyword);
            }
        }
    }
</script>

<style scoped>

</style>