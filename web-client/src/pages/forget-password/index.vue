<template>
    <b-overlay :show="isSubmit" rounded="sm" spinner-variant="primary">
        <ForgetPasswordComponent
                :isSubmit="isSubmit"
                @forgetPassword="forgetPassword" />
    </b-overlay>
</template>

<script>
    import ForgetPasswordComponent from "../../components/ForgetPasswordComponent";
    import {forgetPassword} from "../../services/user_service";
    import {RESPONSE} from "../../services/constants";
    export default {
        name: "ForgetPassword",
        components: {ForgetPasswordComponent},
        data() {
            return {
                isSubmit: false
            }
        },
        methods: {
            async forgetPassword(req) {
                try {
                    this.isSubmit = true;
                    const res = await forgetPassword(req);
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.$bvToast.toast(res.data.message, {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'success',
                            autoHideDelay: 2000
                        });
                        setTimeout(async (self = this) => {
                            await self.$router.push('/login');
                            this.isSubmit = false;
                        }, 1000);
                    } else {
                        this.isSubmit = false;
                        this.$bvToast.toast(res.message, {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'danger',
                            autoHideDelay: 2000
                        });
                    }
                } catch (e) {
                    console.log('Reset password error: ', e);
                    this.isSubmit = false;
                }
            }
        }
    }
</script>

<style scoped>

</style>