<template>
    <div role="tablist">
        <h4 class="text-center text-uppercase font-weight-bold">{{ ($t('common.label.profile')) }}</h4>
        <b-card no-body class="mb-1 profile-card">
            <b-card-header header-tag="header" class="p-1" role="tab">
                <b-button block v-b-toggle.accordion-generation variant="outline" class="text-left">
                    {{ $t('profile.label.generation') }}
                    <span class="collapse-icon">
                        <b-icon icon="caret-right-fill" />
                    </span>
                </b-button>
            </b-card-header>
            <b-collapse id="accordion-generation" visible accordion="generation" role="tabpanel">
                <b-card-body>
                    <ProfileGeneralInformation
                            :profile="profile"
                            :isLoading="isLoading"
                            @save="save" />
                </b-card-body>
            </b-collapse>
        </b-card>

        <b-card no-body class="mb-1 profile-card">
            <b-card-header header-tag="header" class="p-1" role="tab">
                <b-button block v-b-toggle.accordion-change-password variant="outline" class="text-left">
                    {{ $t('profile.label.change_password') }}
                    <span class="collapse-icon">
                        <b-icon icon="caret-right-fill" />
                    </span>
                </b-button>
            </b-card-header>
            <b-collapse id="accordion-change-password" accordion="change-password" role="tabpanel">
                <b-card-body>
                    <ProfileChangePassword
                            :isLoading="isLoading"
                            @changePassword="changePassword"/>
                </b-card-body>
            </b-collapse>
        </b-card>
    </div>
</template>

<script>
    import ProfileGeneralInformation from "./ProfileGeneralInformation";
    import ProfileChangePassword from "./ProfileChangePassword";

    export default {
        name: "ProfileComponent",
        components: {ProfileChangePassword, ProfileGeneralInformation},
        props: [
            'profile',
            'isLoading'
        ],
        methods: {
            save() {
                this.$emit('save');
            },
            changePassword(data) {
                this.$emit('changePassword', data);
            }
        }
    }
</script>

<style lang="scss" scoped>
    .profile-card {
        margin: 1rem auto;
        max-width: 45rem;
        .profile-form {
            padding: 0.5rem 1rem;
        }
    }
</style>