<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div class="navbar-header-wrapper">
        <b-navbar toggleable="lg" type="dark" variant="dark" fixed="top" class="header-container">
            <b-navbar-brand @click="redirectTo('/home')" class="has-link">
              <b-img src="../assets/logo.png" height="50" :alt="$t('common.label.slogan')" />
            </b-navbar-brand>

            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

            <b-collapse id="nav-collapse" v-model="isNavbarCollapseOpen" is-nav>
                <b-navbar-nav>
                    <b-nav-item @click="redirectTo('/home')">{{ $t('common.label.home') }}</b-nav-item>
                    <b-nav-item-dropdown left class="is-desktop">
                        <template v-slot:button-content>
                            {{ $t('common.label.module') }}
                        </template>
                        <b-dropdown-item @click="redirectTo('/chat-list')" :active="$route.path === '/chat-list'">
                            <b-icon icon="chat-dots"></b-icon> {{ $t('common.label.chat_list') }}
                        </b-dropdown-item>
                        <b-dropdown-item @click="redirectTo()" :active="$route.path === '/module2'">
                            <b-icon icon="puzzle"></b-icon> Module 2
                        </b-dropdown-item>
                    </b-nav-item-dropdown>
                    <b-nav-item-dropdown left class="is-desktop">
                        <template v-slot:button-content>
                            {{ $t('common.label.help') }}
                        </template>
                        <b-dropdown-item @click="redirectTo('/about-me')" :active="$route.path === '/about-me'">
                            <b-icon icon="emoji-sunglasses" ></b-icon> {{ $t('common.label.about_me') }}
                        </b-dropdown-item>
                        <b-dropdown-item @click="redirectTo('/contact-us')" :active="$route.path === '/contact-us'">
                            <b-icon icon="question-diamond"></b-icon> {{ $t('common.label.contact_us') }}
                        </b-dropdown-item>
                    </b-nav-item-dropdown>
                    <b-nav-item @click="redirectTo('/chat-list')" class="is-mobile">
                        {{ $t('common.label.chat_list') }}
                    </b-nav-item>
                    <b-nav-item @click="redirectTo('/about-me')" class="is-mobile">
                        {{ $t('common.label.about_me') }}
                    </b-nav-item>
                    <b-nav-item @click="redirectTo('/contact-us')" class="is-mobile">
                        {{ $t('common.label.contact_us') }}
                    </b-nav-item>
                </b-navbar-nav>

                <!-- Right aligned nav items -->
                <b-navbar-nav class="ml-auto">
                    <b-nav-form @submit.stop.prevent="searchFriend">
                        <b-form-input size="sm" class="mr-sm-2"
                                      v-model="keyword"
                                      type="search"
                                      :placeholder="$t('common.label.search_friend')"></b-form-input>
                    </b-nav-form>

                    <b-nav-item-dropdown right class="is-desktop">
                        <!-- Using 'button-content' slot -->
                        <template v-slot:button-content>
                            <b-avatar variant="success" class="text-uppercase"
                                      :text="$store.getters.name ? $store.getters.name.charAt(0) : ''"></b-avatar> <em>{{ $store.getters.name }} </em>
                        </template>
                        <b-dropdown-item @click="redirectTo('/profile')" :active="$route.path === '/profile'">
                            <b-icon icon="person-square"></b-icon> {{ $t('common.label.profile') }}
                        </b-dropdown-item>
                        <b-dropdown-item @click="redirectTo('/login-history')" :active="$route.path === '/login-history'">
                            <b-icon icon="arrow-counterclockwise"></b-icon> {{ $t('common.label.login_history') }}
                        </b-dropdown-item>
                        <b-dropdown-item @click="logout">
                            <b-icon icon="power"></b-icon> {{ $t('common.label.sign_out') }}
                        </b-dropdown-item>
                    </b-nav-item-dropdown>
                    <b-nav-text class="is-mobile navbar-header-username">
                        <b-avatar variant="success" class="text-uppercase"
                                  :text="$store.getters.name ? $store.getters.name.charAt(0) : ''">
                        </b-avatar> <em>{{ $store.getters.name }} </em>
                    </b-nav-text>
                    <b-nav-item @click="redirectTo('/profile')" class="is-mobile ml-2">
                        <b-icon icon="person-square"></b-icon> {{ $t('common.label.profile') }}
                    </b-nav-item>
                    <b-nav-item @click="redirectTo('/login-history')" class="is-mobile ml-2">
                        <b-icon icon="arrow-counterclockwise"></b-icon> {{ $t('common.label.login_history') }}
                    </b-nav-item>
                    <b-nav-item @click="logout" class="is-mobile ml-2">
                        <b-icon icon="power"></b-icon> {{ $t('common.label.sign_out') }}
                    </b-nav-item>
                </b-navbar-nav>
            </b-collapse>
        </b-navbar>
        <div v-if="isNavbarCollapseOpen" @click="isNavbarCollapseOpen = false" class="navbar-backdrop"></div>
    </div>
</template>

<script>
    export default {
        name: "Header",
        props: [
            'user'
        ],
        data() {
            return {
                keyword: '',
                isNavbarCollapseOpen: false
            }
        },
        mounted() {
            if (this.$route.name === 'FindFriend' && this.keyword === '') {
                this.keyword = this.$route.query.keyword
            }
        },
        methods: {
            logout() {
                this.$emit('logout');
            },
            searchFriend() {
                this.isNavbarCollapseOpen = false
                this.$emit('searchFriend', this.keyword);
            },
          redirectTo(path = '') {
              if (path === '' || path === this.$route.path) {
                return
              }
              this.$router.push(path)
          }
        }
    }
</script>

<style lang="scss" scoped>
.navbar-header-wrapper {
    .navbar-backdrop {
        z-index: 1029;
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
    }

    @media screen and (min-width: 992px) {
        .is-desktop {
            display: block;
        }
        .is-mobile {
            display: none;
        }
    }
    @media screen and (max-width: 991px) {
        .is-desktop {
            display: none;
        }
        .is-mobile {
            display: block;
        }
        input {
            width: calc(100vw - 2rem);
        }
    }
}
</style>