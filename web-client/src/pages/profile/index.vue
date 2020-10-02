<template>
    <div class="profile-page">
        <b-breadcrumb :items="crumbItems"></b-breadcrumb>
        <b-overlay :show="isLoading" rounded="sm" spinner-variant="primary">
            <ProfileComponent
                    :profile="profile"
                    :isLoading="isLoading"
                    @save="save"
                    @changePassword="changePassword" />
        </b-overlay>
    </div>
</template>

<script>
    import {changePassword, getCurrentUser, signOut} from "../../services/user_service";
    import {RESPONSE, VARIANT} from "../../services/constants";
    import ProfileComponent from "../../components/profile/ProfileComponent";
    import { update } from "../../services/user_service";
    import ToastHelper from "@/helper/ToastHelper";

    export default {
        name: "Profile",
        components: {ProfileComponent},
        data() {
            return {
                oldProfile: {},
                profile: null,
                crumbItems: [
                    {
                        text: this.$t('common.label.home'),
                        to: { name: 'Home' }
                    },
                    {
                        text: this.$t('common.label.profile'),
                        active: true
                    }
                ],
                isLoading: true
            }
        },
        async mounted() {
            await this.getProfile();
        },
        methods: {
            async getProfile() {
                try {
                    this.isLoading = true;
                    const res = await getCurrentUser();
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.profile = res.data;
                        this.oldProfile = {...res.data};
                    } else {
                        console.log('Get profile error: ', res.message);
                    }
                } catch (e) {
                    console.log('Get profile error: ', e);
                } finally {
                    this.isLoading = false;
                }
            },
            async save() {
                try {
                    this.isLoading = true;
                    if (JSON.stringify(this.profile) === JSON.stringify(this.oldProfile)) {
                        ToastHelper.message(this.$t('common.message.update_success'))
                        return;
                    }

                    const res = await update(this.profile);
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.oldProfile = {...this.profile};
                        ToastHelper.message(this.$t('common.message.update_success'))
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch (e) {
                    console.log('Update User error: ', e);
                } finally {
                    this.isLoading = false;
                }
            },

            async changePassword(data) {
                try {
                    this.isLoading = true;
                    data.id = this.profile.id;
                    const res = await changePassword(data);
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.oldProfile = {...this.profile};
                        await signOut();
                        await ToastHelper.message(this.$t('profile.message.change_password_success'))
                        setTimeout(async (self = this) => {
                            await self.$router.push('/login');
                            this.isLoading = false;
                        }, 2000);
                    } else {
                        this.isLoading = false;
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch (e) {
                    console.log('Change password error: ', e);
                    this.isLoading = false;
                }
            }
        }
    }
</script>

<style scoped>

</style>