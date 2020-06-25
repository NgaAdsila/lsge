<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <b-navbar toggleable="lg" type="dark" variant="dark" fixed="top" class="header-container">
        <b-navbar-brand href="/home">
            <b-img src="../assets/logo.png" height="50" :alt="$t('common.label.slogan')" />
        </b-navbar-brand>

        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

        <b-collapse id="nav-collapse" is-nav>
            <b-navbar-nav>
                <b-nav-item href="/home">{{ $t('common.label.home') }}</b-nav-item>
                <b-nav-item-dropdown left>
                    <template v-slot:button-content>
                        {{ $t('common.label.module') }}
                    </template>
                    <b-dropdown-item disabled>
                        <strong><b-icon icon="book" ></b-icon> {{ $t('common.label.book_store') }}</strong>
                    </b-dropdown-item>
                    <b-dropdown-item href="/book-store/old-home" :active="$route.path === '/book-store/old-home'">
                        <b-icon icon="hash" ></b-icon> {{ $t('common.label.home_page') }}
                    </b-dropdown-item>
                    <b-dropdown-item href="/book-store/old-admin" :active="$route.path === '/book-store/old-admin'">
                        <b-icon icon="hash" ></b-icon> {{ $t('common.label.admin_page') }}
                    </b-dropdown-item>
                    <b-dropdown-divider></b-dropdown-divider>

                </b-nav-item-dropdown>
                <b-nav-item-dropdown left>
                    <template v-slot:button-content>
                        {{ $t('common.label.help') }}
                    </template>
                    <b-dropdown-item href="/about-me" :active="$route.path === '/about-me'">
                        <b-icon icon="emoji-sunglasses" ></b-icon> {{ $t('common.label.about_me') }}
                    </b-dropdown-item>
                    <b-dropdown-item href="/contact-us" :active="$route.path === '/contact-us'">
                        <b-icon icon="question-diamond"></b-icon> {{ $t('common.label.contact_us') }}
                    </b-dropdown-item>
                </b-nav-item-dropdown>
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
                        <b-avatar variant="success" class="text-uppercase"
                                  :text="$store.getters.name ? $store.getters.name.charAt(0) : ''"></b-avatar> <em>{{ $store.getters.name }} </em>
                    </template>
                    <b-dropdown-item href="/profile" :active="$route.path === '/profile'">
                        <b-icon icon="person-square"></b-icon> {{ $t('common.label.profile') }}
                    </b-dropdown-item>
                    <b-dropdown-item href="/login-history" :active="$route.path === '/login-history'">
                        <b-icon icon="arrow-counterclockwise"></b-icon> {{ $t('common.label.login_history') }}
                    </b-dropdown-item>
                    <b-dropdown-item @click="logout">
                        <b-icon icon="power"></b-icon> {{ $t('common.label.sign_out') }}
                    </b-dropdown-item>
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