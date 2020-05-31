<template>
    <LoginComponent
            @login="login" />
</template>

<script>
    import LoginComponent from '../components/LoginComponent';
    import { login } from '../services/user_service.js';
    export default {
        name: "Login",
        components: {LoginComponent},
        methods: {
            login: async (data) => {
                const user = await login(data);
                console.log('USER:', user);
            }
        }
    }
</script>

<style scoped>

</style>