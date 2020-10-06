<template>
    <div class="profile-component">
        <div class="profile-header">
            <h4 class="text-center text-uppercase font-weight-bold">{{ ($t('common.label.profile')) }}</h4>
            <b-button v-show="collapses.some(t => !!t)"
                      variant="outline-primary" class="profile-collapse-all"
                      @click="collapseAll">
                {{ $t('common.label.collapse_all') }}
            </b-button>
        </div>
        <b-card no-body class="mb-1 profile-card">
            <b-card-header header-tag="header" class="p-1" role="tab">
                <b-button block v-b-toggle.accordion-generation variant="outline" class="text-left">
                    {{ $t('profile.label.generation') }}
                    <span class="collapse-icon">
                        <b-icon icon="caret-right-fill" />
                    </span>
                </b-button>
            </b-card-header>
            <b-collapse id="accordion-generation" accordion="generation" v-model="collapses[0]">
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
            <b-collapse id="accordion-change-password" accordion="change-password" v-model="collapses[1]">
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
        data() {
            return {
                collapses: [true, false]
            }
        },
        methods: {
            save() {
                this.$emit('save');
            },
            changePassword(data) {
                this.$emit('changePassword', data);
            },
            collapseAll() {
                this.collapses = [false, false]
            }
        }
    }
</script>

<style lang="scss" scoped>
    .profile-component {
        margin: 1rem auto;
        max-width: 45rem;
        .profile-header {
            position: relative;
            margin-bottom: 1rem;
            .profile-collapse-all {
                position: absolute;
                top: 0;
                right: 0;
            }
        }
        .profile-card {
            .profile-form {
                padding: 0.5rem 1rem;
            }
        }
    }
</style>