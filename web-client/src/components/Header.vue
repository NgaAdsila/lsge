<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <b-navbar toggleable="lg" type="dark" variant="info" fixed="top" class="header-container">
        <b-navbar-brand href="/home">
            <b-img src="../assets/logo.png" height="50" :alt="$t('common.label.slogan')" />
        </b-navbar-brand>

        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

        <b-collapse id="nav-collapse" is-nav>
            <b-navbar-nav>
                <b-nav-item href="#">Link</b-nav-item>
                <b-nav-item href="#" disabled>Disabled</b-nav-item>
            </b-navbar-nav>

            <!-- Right aligned nav items -->
            <b-navbar-nav class="ml-auto">
                <b-nav-form @submit.stop.prevent="search">
                    <b-form-input size="sm" class="mr-sm-2"
                                  v-model="keyword"
                                  :placeholder="$t('common.label.search')"></b-form-input>
                </b-nav-form>

                <b-nav-item-dropdown right>
                    <!-- Using 'button-content' slot -->
                    <template v-slot:button-content>
                        <b-avatar variant="success"></b-avatar> <em>{{ user.name }} </em>
                    </template>
                    <b-dropdown-item href="#">{{ $t('common.label.profile') }}</b-dropdown-item>
                    <b-dropdown-item href="/login-history">{{ $t('common.label.login_history') }}</b-dropdown-item>
                    <b-dropdown-item @click="logout"><b-icon icon="power"></b-icon> {{ $t('common.label.sign_out') }}</b-dropdown-item>
                </b-nav-item-dropdown>
            </b-navbar-nav>
        </b-collapse>
    </b-navbar>
</template>

<script>
    export default {
        name: "Header",
        props: [
            'user'
        ],
        data() {
            return {
                keyword: ''
            }
        },
        methods: {
            logout() {
                this.$emit('logout');
            },
            search() {
                this.$emit('search', this.keyword);
            }
        }
    }
</script>

<style scoped>

</style>