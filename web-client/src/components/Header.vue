<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div class="navbar-header-wrapper">
        <b-navbar toggleable="lg" type="dark" variant="dark" fixed="top" class="header-container">
            <b-navbar-brand @click="redirectTo('Home')" class="has-link">
              <b-img src="../assets/logo.png" height="50" :alt="$t('common.label.slogan')" />
            </b-navbar-brand>

            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

            <b-collapse id="nav-collapse" v-model="isNavbarCollapseOpen" is-nav>
                <b-navbar-nav>
                    <b-nav-item @click="redirectTo('Home')" :active="$route.name === 'Home'">
                        {{ $t('common.label.home') }}
                    </b-nav-item>
                    <b-nav-item @click="redirectTo('AboutMe')"
                                :active="$route.name === 'AboutMe'">
                        {{ $t('common.label.about_me') }}
                    </b-nav-item>
                    <b-nav-item @click="redirectTo('ContactUs')"
                                :active="$route.name === 'ContactUs'">
                        {{ $t('common.label.contact_us') }}
                    </b-nav-item>
                </b-navbar-nav>

                <!-- Right aligned nav items -->
                <b-navbar-nav class="ml-auto">
                    <b-nav-text class="is-mobile navbar-header-username">
                        <div class="user-name has-link" @click="redirectTo('Profile')">
                            <Avatar :avatar="$store.getters.avatar"
                                    :color="$store.getters.color"
                                    :name="$store.getters.name"
                                    default-color="#28a745"
                            /> <em>{{ $store.getters.name }} </em>
                        </div>
                        <div @click="logout" class="user-action-logout has-link">
                            <b-icon icon="power" scale="1.5"></b-icon>
                        </div>
                    </b-nav-text>
                    <b-nav-item @click="redirectTo('LoginHistory')" class="is-mobile"
                                :active="$route.name === 'LoginHistory'">
                        {{ $t('common.label.login_history') }}
                    </b-nav-item>
                    <b-nav-item v-if="isManagementUser" class="is-mobile"
                                @click="redirectTo('ManagerHome')"
                                :active="$route.name === 'ManagerHome'">
                        {{ $t('manager.label.button_title') }}
                    </b-nav-item>
                    <b-nav-form @submit.stop.prevent="searchFriend">
                        <b-input-group class=" mr-sm-2">
                            <b-form-input size="sm" class="nav-header-search-friend"
                                          v-model="keyword"
                                          type="search"
                                          :placeholder="$t('common.label.search_friend')"></b-form-input>
                            <b-input-group-append>
                                <b-button size="sm" type="submit" variant="outline-secondary">
                                    <b-icon icon="search" scale="0.8"></b-icon>
                                </b-button>
                            </b-input-group-append>
                        </b-input-group>
                    </b-nav-form>

                    <b-nav-item-dropdown right class="is-desktop">
                        <!-- Using 'button-content' slot -->
                        <template v-slot:button-content>
                            <Avatar :avatar="$store.getters.avatar"
                                    :color="$store.getters.color"
                                    :name="$store.getters.name"
                                    default-color="#28a745"
                            /> <em>{{ $store.getters.name }} </em>
                        </template>
                        <b-dropdown-item @click="redirectTo('Profile')" :active="$route.name === 'Profile'">
                            <b-icon icon="person-square"></b-icon> {{ $t('common.label.profile') }}
                        </b-dropdown-item>
                        <b-dropdown-item @click="redirectTo('LoginHistory')" :active="$route.name === 'LoginHistory'">
                            <b-icon icon="arrow-counterclockwise"></b-icon> {{ $t('common.label.login_history') }}
                        </b-dropdown-item>
                        <b-dropdown-item v-if="isManagementUser"
                                         @click="redirectTo('ManagerHome')" :active="$route.name === 'ManagerHome'">
                            <b-icon icon="gear"></b-icon> {{ $t('manager.label.button_title') }}
                        </b-dropdown-item>
                        <b-dropdown-item @click="logout">
                            <b-icon icon="power"></b-icon> {{ $t('common.label.sign_out') }}
                        </b-dropdown-item>
                    </b-nav-item-dropdown>
                </b-navbar-nav>
            </b-collapse>
        </b-navbar>
        <div v-if="isNavbarCollapseOpen" @click="isNavbarCollapseOpen = false" class="navbar-backdrop"></div>
    </div>
</template>

<script>
    import Avatar from "@/components/common/Avatar";
    import {USER_ROLES} from "@/services/role";
    export default {
        name: "Header",
        components: {Avatar},
        props: [
            'currentUserRole'
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
            redirectTo(name = '') {
                  this.isNavbarCollapseOpen = false
                  if (name === '' || name === this.$route.name) {
                        return
                  }
                  this.$router.push({name: name})
            },
            isManagementUser() {
                return this.currentUserRole === USER_ROLES.ADMIN.ROLE
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
            &.navbar-header-username {
                display: flex;
                justify-content: space-between;
                border-top: 1px dotted lightgrey;
                margin-right: 0.25rem;
                .user-name {
                    white-space: nowrap;
                    text-overflow: ellipsis;
                    overflow: hidden;
                    width: calc(100vw - 3.5rem);
                }
                .user-action-logout {
                    line-height: 2.125rem;
                }
            }
        }
        //.nav-header-search-friend {
        //    width: calc(100vw - 2.5rem);
        //}
    }
}
</style>