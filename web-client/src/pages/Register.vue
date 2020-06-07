<template>
    <b-container>
        <RegisterComponent
                :isSubmit="isSubmit"
                @register="onRegister"/>
    </b-container>
</template>

<script>
    import RegisterComponent from "../components/RegisterComponent";
    import { register } from '../services/user_service';
    import {RESPONSE} from "../services/constants";
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
                        this.$bvToast.toast(this.$t('register.message.success'), {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'success',
                            autoHideDelay: 2000
                        });
                        await this.$router.push('/login');
                    } else {
                        console.log(res);
                        this.$bvToast.toast(res.message, {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'danger',
                            autoHideDelay: 2000
                        });
                    }
                } catch (e) {
                    console.log(e);
                    console.log('Register ERROR: ', e);
                } finally {
                    this.isSubmit = false;
                }
            }
        }
    }
</script>

<style scoped>

</style>