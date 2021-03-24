<template>
    <b-modal
            id="edit-profile-modal"
            scrollable
            :title="$t('profile.label.edit_profile')"
            size="lg"
            centered
            @show="resetModal"
            @hidden="resetModal"
            hide-footer
    >
        <b-overlay :show="isLoading" rounded="sm" spinner-variant="primary">
            <ProfileGeneralInformation
                    :profile="profile"
                    :isLoading="isLoading"
                    :profileModel="profileModel"
                    @addFileToModel="addFileToModel"
                    @updateAvatarLinkToModel="updateAvatarLinkToModel"
                    @save="save" />
        </b-overlay>
    </b-modal>
</template>

<script>
    import ProfileGeneralInformation from "../../profile/ProfileGeneralInformation";
    export default {
        name: "EditProfileModal",
        components: {ProfileGeneralInformation},
        props: [
            'profile',
            'isLoading'
        ],
        data() {
            return {
                profileModel: {
                    name: null,
                    username: null,
                    email: null,
                    color: null,
                    avatarFile: null,
                    avatar: null
                }
            }
        },
        methods: {
            save() {
                this.$emit('save', this.profileModel)
            },
            resetModal() {
                this.profileModel = {...this.profile}
                this.profileModel.avatarFile = null
            },
            addFileToModel(file) {
                this.profileModel.avatarFile = file
                this.$forceUpdate()
            },
            updateAvatarLinkToModel(file) {
                if (!file) {
                    return
                }
                this.profileModel.avatar = URL.createObjectURL(file)
                this.$forceUpdate()
            }
        }
    }
</script>

<style scoped>

</style>