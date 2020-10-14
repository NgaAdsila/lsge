<template>
    <div class="manager-user-header-component">
        <h4 class="text-center text-uppercase font-weight-bold">{{ ($t('manager.label.user_list')) }}</h4>
        <div class="d-flex justify-content-sm-between">
            <div v-if="!selectedRows.length" class="font-weight-bold user-list-total-record">
                {{ $t('common.message.total_record', { number: paging.total }) }}
            </div>
            <div v-else>
                <b-button-group>
                    <b-button v-show="activatedUsers.length"
                              variant="outline-primary"
                              @click="resetPasswordUsers(activatedUsers)"
                              v-b-tooltip.hover="$t('manager.action.selected.reset_password', { number: activatedUsers.length })">
                        <b-icon icon="lock-fill"></b-icon>
                    </b-button>
                    <b-button v-show="activatedUsers.length"
                              variant="outline-primary"
                              @click="updateRoleUsers(activatedUsers)"
                              v-b-tooltip.hover="$t('manager.action.selected.update_role', { number: activatedUsers.length })">
                        <b-icon icon="shield-lock-fill"></b-icon>
                    </b-button>
                    <b-button v-show="deletedUsers.length"
                              variant="outline-primary"
                              @click="activeUsers(deletedUsers)"
                              v-b-tooltip.hover="$t('manager.action.selected.active_user', { number: deletedUsers.length })">
                        <b-icon icon="person-check-fill"></b-icon>
                    </b-button>
                    <b-button v-show="activatedUsers.length"
                              variant="outline-primary"
                              @click="bandUsers(activatedUsers)"
                              v-b-tooltip.hover="$t('manager.action.selected.band_user', { number: activatedUsers.length })">
                        <b-icon icon="person-x-fill"></b-icon>
                    </b-button>
                </b-button-group>
            </div>
            <div class="text-right">
                <b-input-group class="search-key-word">
                    <b-form-input v-model="req.keyword"
                                  type="search"
                                  maxlength="100"
                                  :placeholder="$t('common.placeholder.keyword')"
                                  class="active-search-keyword"
                                  @search="search"
                                  @keydown.enter.native="search"></b-form-input>
                    <template v-slot:append>
                        <b-button v-b-toggle.sidebar-search><b-icon icon="list"></b-icon></b-button>
                        <b-sidebar id="sidebar-search" :title="$t('common.label.search_form')" right shadow>
                            <b-form @submit.stop.prevent="search" class="text-justify search-form">
                                <hr/>
                                <b-form-group :label="$t('common.label.keyword')"
                                              label-for="input-keyword">
                                    <b-form-input id="input-keyword" size="sm"
                                                  type="search"
                                                  v-model="req.keyword"
                                                  :placeholder="$t('common.placeholder.keyword')"
                                                  maxlength="100"></b-form-input>
                                </b-form-group>
                                <b-form-group :label="$t('manager.label.search.role')"
                                              label-for="input-roles">
                                    <b-form-select id="input-roles"
                                                   v-model="req.roles"
                                                   :options="roleOptions" multiple></b-form-select>
                                </b-form-group>
                                <b-form-group>
                                    <b-form-checkbox
                                            id="checkbox-status"
                                            v-model="req.status"
                                            name="checkbox-1"
                                            :value="false"
                                            :unchecked-value="null">
                                        {{ $t('manager.label.search.status') }}
                                    </b-form-checkbox>
                                </b-form-group>
                                <hr/>
                                <div class="text-center">
                                    <b-button variant="danger"
                                              inline
                                              class="button-reset"
                                              @click="resetReq">{{ $t('common.label.reset') }}</b-button>
                                    <b-button type="submit"
                                              inline
                                              v-b-toggle.sidebar-search
                                              variant="outline-primary">{{ $t('common.label.search') }}</b-button>
                                </div>
                            </b-form>
                        </b-sidebar>
                    </template>
                </b-input-group>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "ManagerUserListHeaderComponent",
        props: [
            'req',
            'paging',
            'roleOptions',
            'selectedRows'
        ],
        computed: {
            deletedUsers: function () {
                return this.selectedRows.filter(u => u.status) || []
            },
            activatedUsers: function () {
                return this.selectedRows.filter(u => !u.status) || []
            }
        },
        methods: {
            search() {
                this.$emit('search', this.req);
            },
            resetReq() {
                this.$emit('resetReq');
            },
            activeUsers(users) {
                this.$emit('activeUsers', users)
            },
            bandUsers(users) {
                this.$emit('bandUsers', users)
            },
            resetPasswordUsers(users) {
                this.$emit('resetPasswordUsers', users)
            },
            updateRoleUsers(users) {
                this.$emit('updateRoleUsers', users)
            }
        }
    }
</script>

<style lang="scss" scoped>
.manager-user-header-component {
    margin: 1rem;
    .user-list-total-record {
        line-height: 2.375rem;
    }
    .search-key-word {
        max-width: 22rem;

        @media (min-width: 574px) {
            float: right!important;
        }

        input.active-search-keyword {
            @media (max-width: 486px) {
                display: none;
            }
        }
    }
    #sidebar-search {
        .search-form {
            padding: 0 1rem;
        }
        .search-range-symbol {
            margin-left: 49%;
        }
        .button-reset {
            margin-right: 1rem;
        }
    }
}
</style>
<style lang="scss">
.manager-user-header-component {
    #sidebar-search {
        top: 4.5rem;
        height: calc(100vh - 4.75rem);
    }
}
</style>