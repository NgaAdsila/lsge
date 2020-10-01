<template>
    <b-container>
        <b-overlay :show="isSubmit" rounded="sm" spinner-variant="primary">
            <RegisterComponent
                    :isSubmit="isSubmit"
                    @register="onRegister"/>
        </b-overlay>
    </b-container>
</template>

<script>
    import RegisterComponent from "../components/RegisterComponent";
    import { register } from '../services/user_service';
    import {RESPONSE, VARIANT} from "../services/constants";
    import ToastHelper from "@/helper/ToastHelper";
    export default {
        name: "Register",
        components: {RegisterComponent},
        data() {
            return {
                isSubmit: false
            }
        },
        methods: {
            async onRegister(data = {}) {
                try {
                    this.isSubmit = true;
                    const res = await register(data);
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        ToastHelper.message(this.$t('register.message.success'))
                        setTimeout(async (self = this) => {
                            await self.$router.push('/login');
                            this.isSubmit = false;
                        }, 1000);
                    } else {
                        this.isSubmit = false;
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch (e) {
                    console.log('Register ERROR: ', e);
                    this.isSubmit = false;
                }
            }
        }
    }
</script>

<style scoped>

</style>