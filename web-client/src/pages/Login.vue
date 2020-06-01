<template>
    <LoginComponent
            @login="login" />
</template>

<script>
    import LoginComponent from '../components/LoginComponent';
    import { login } from '../services/user_service';
    import { RESPONSE } from '../services/constants';

    export default {
        name: "Login",
        components: {LoginComponent},
        methods: {
            async login(data) {
                try {
                    const res = await login(data);
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        const jwt = res.data.tokenType + ' ' + res.data.accessToken,
                            currentUser = res.data.currentUser;
                        this.$store.commit('doLogin', {
                            name: currentUser.name,
                            role: currentUser.authorities[0].authority
                        });
                        this.$store.commit('saveJwt', jwt);
                        this.$router.push('/');
                    } else {
                        console.log('Login ERROR: ', res.message);
                    }
                } catch (e) {
                    console.log('Login ERROR: ', e);
                }
            }
        }
    }
</script>

<style scoped>

</style>