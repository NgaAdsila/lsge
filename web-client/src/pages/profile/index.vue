<template>
    <div class="profile-page">
        <b-overlay :show="isLoading" rounded="sm" spinner-variant="primary">
            <UserProfile
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
    import { update } from "../../services/user_service";
    import ToastHelper from "@/helper/ToastHelper";
    import UserProfile from "../../components/profile/Profile";

    export default {
        name: "Profile",
        components: {UserProfile},
        data() {
            return {
                profile: null,
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
                        this.profile.avatarFile = null
                    } else {
                        console.log('Get profile error: ', res.message);
                    }
                } catch (e) {
                    console.log('Get profile error: ', e);
                } finally {
                    this.isLoading = false;
                }
            },
            async save(profileModel) {
                try {
                    this.isLoading = true;
                    if (this.isNotChangeProfile(profileModel)) {
                        this.$bvModal.hide('edit-profile-modal');
                        ToastHelper.message(this.$t('common.message.update_success'))
                        return;
                    }
                    const res = await update(profileModel);
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.profile = {...this.profile, ...profileModel}
                        this.$bvModal.hide('edit-profile-modal');
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

            isNotChangeProfile(profileModel) {
                return this.profile.name === profileModel.name.trim()
                    && this.profile.email === profileModel.email.trim()
                    && this.profile.color === profileModel.color
                    && this.profile.username === profileModel.username.trim()
                    && !profileModel.avatarFile
            },

            async changePassword(data) {
                try {
                    this.isLoading = true;
                    data.id = this.profile.id;
                    const res = await changePassword(data);
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
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