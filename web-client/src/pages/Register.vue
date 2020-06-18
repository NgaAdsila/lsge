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
                    console.log('Register ERROR: ', e);
                    this.isSubmit = false;
                }
            }
        }
    }
</script>

<style scoped>

</style>