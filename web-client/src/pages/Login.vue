<template>
    <b-container>
        <LoginComponent
                @login="login" />
    </b-container>
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
                            variant: 'success',
                            autoHideDelay: 2000
                        });
                        await this.$router.push('/');
                        return;
                    }
                } catch (e) {
                    console.log('Login ERROR: ', e);
                }
                this.$bvToast.toast(this.$t('login.message.error'), {
                    title: this.$t('common.toast.title'),
                    toaster: 'b-toaster-top-center',
                    solid: true,
                    variant: 'danger',
                    autoHideDelay: 2000
                });
            }
        }
    }
</script>

<style scoped>

</style>