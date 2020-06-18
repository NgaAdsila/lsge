<template>
    <b-container>
        <b-overlay :show="isSubmit" rounded="sm" spinner-variant="primary">
            <LoginComponent
                    :isSubmit="isSubmit"
                    @login="login" />
        </b-overlay>
    </b-container>
</template>

<script>
    import LoginComponent from '../components/LoginComponent';
    import { login } from '../services/user_service';
    import { RESPONSE } from '../services/constants';

    export default {
        name: "Login",
        components: {LoginComponent},
        data() {
            return {
                isSubmit: false
            }
        },
        methods: {
            async login(data) {
                try {
                    this.isSubmit = true;
                    const res = await login(data);
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.$bvToast.toast(this.$t('login.message.success'), {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'success',
                            autoHideDelay: 2000
                        });
                        setTimeout(async (self = this) => {
                            await self.$router.push('/');
                            this.isSubmit = false;
                        }, 1000);
                        return;
                    }
                } catch (e) {
                    console.log('Login ERROR: ', e);
                }
                this.isSubmit = false;
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