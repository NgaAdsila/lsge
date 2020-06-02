<template>
    <div>
        <Header
                :user="user"
                @logout="logout"/>
        <b-container>
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
            }
        }
    }
</script>

<style scoped>

</style>