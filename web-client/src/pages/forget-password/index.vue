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
    import {RESPONSE, VARIANT} from "../../services/constants";
    import ToastHelper from "@/helper/ToastHelper";
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
                        ToastHelper.message(res.data.message)
                        setTimeout(async (self = this) => {
                            await self.$router.push('/login');
                            this.isSubmit = false;
                        }, 1000);
                    } else {
                        this.isSubmit = false;
                        ToastHelper.message(res.message, VARIANT.DANGER)
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