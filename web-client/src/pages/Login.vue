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
                        this.$bvToast.toast(this.$t('login.message.success'), {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            appendToast: append,
                            variant: 'success',
                            'auto-hide-delay': 2000
                        });
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