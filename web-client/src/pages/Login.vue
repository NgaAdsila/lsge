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
    import { login } from '@/services/user_service';
    import {RESPONSE, VARIANT} from '@/services/constants';
    import ToastHelper from "@/helper/ToastHelper";

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
                        ToastHelper.message(this.$t('login.message.success'))
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
                ToastHelper.message(this.$t('login.message.error'), VARIANT.DANGER)
            }
        }
    }
</script>

<style scoped>

</style>