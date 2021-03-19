<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <div class="manager-user-list-component">
        <b-table
                id="manager-user-table"
                ref="managerUserTable"
                selectable
                select-mode="multi"
                selected-variant="warning"
                hover
                :items="userList"
                :fields="fields"
                :per-page="paging.limit"
                head-variant='light'
                responsive
                show-empty
                @row-selected="onRowSelected"
                :tbody-tr-class="rowClass"
        >
            <template v-slot:empty="">
                <b-alert variant="warning" show>{{ $t('common.message.no_data') }}</b-alert>
            </template>
            <template v-slot:head(selected)="data">
                <b-icon :icon="selectedModeIcon"
                        class="has-link"
                        variant="primary" @click="handleSelectAll"></b-icon>{{ data.label }}
            </template>
            <template v-slot:cell(selected)="{ rowSelected }">
                <b-icon :icon="rowSelected ? 'check-square-fill' : 'square'" variant="primary"></b-icon>
            </template>
            <template v-slot:cell(roles)="data">
                {{ data.item.roles ? data.item.roles.map(r => r.text).join(', ') : '' }}
            </template>
            <template v-slot:cell(onlineStatus)="data">
                <b v-show="isOnlineUser(data.item.id)">{{$t('manager.label.online.on')}}</b>
                <span v-show="!isOnlineUser(data.item.id)">{{$t('manager.label.online.off')}}</span>
            </template>
            <template v-slot:cell(action)="data">
                <b-dropdown size="sm" variant="link"
                            class="manager-user-action-dropdown"
                            toggle-class="text-decoration-none" no-caret right>
                    <template v-slot:button-content>
                        <b-icon icon="three-dots" variant="primary"></b-icon>
                    </template>
                    <b-dropdown-item v-show="!data.item.status" @click="resetPasswordUsers(data.item)">
                        {{$t('manager.action.reset_password')}}
                    </b-dropdown-item>
                    <b-dropdown-item v-show="!data.item.status" @click="updateRoleUsers(data.item)">
                        {{$t('manager.action.update_role')}}
                    </b-dropdown-item>
                    <b-dropdown-item v-show="data.item.status" @click="activeUsers(data.item)">
                        {{$t('manager.action.active_user')}}
                    </b-dropdown-item>
                    <b-dropdown-item v-show="!data.item.status" @click="bandUsers(data.item)">
                        {{$t('manager.action.band_user')}}
                    </b-dropdown-item>
                </b-dropdown>
            </template>
        </b-table>
        <b-pagination
                v-if="paging.total / paging.limit > 1"
                v-model="paging.page"
                :total-rows="paging.total"
                :per-page="paging.limit"
                aria-controls="manager-user-table"
                first-number
                last-number
                pills
                align="center"
                @change="changePage"
        ></b-pagination>
    </div>
</template>

<script>
    import {SELECTED_MODE} from "@/services/constants";

    export default {
        name: "ManagerUserListComponent",
        props: [
            'userList',
            'paging',
            'fields',
            'onlineUsers',
            'selectedAll'
        ],
        computed: {
            selectedModeIcon: function () {
                return this.selectedAll === SELECTED_MODE.NONE
                    ? 'square'
                    : (this.selectedAll === SELECTED_MODE.ALL ? 'check-square-fill' : 'dash-square-fill')
            }
        },
        methods: {
            onRowSelected(items) {
                this.$emit('onRowSelected', items)
            },
            handleSelectAll() {
                if (this.selectedAll !== SELECTED_MODE.ALL) {
                    this.$refs.managerUserTable.selectAllRows()
                } else {
                    this.$refs.managerUserTable.clearSelected()
                }
            },
            changePage(page) {
                if (page === this.paging.page) {
                    return;
                }
                this.$emit('changePage', page);
            },
            isOnlineUser(userId) {
                return this.onlineUsers.length && this.onlineUsers.some(u => u.id === userId)
            },
            rowClass(item, type) {
                if (!item || type !== 'row') return
                if (item.status) return 'user-item-deleted'
            },
            activeUsers(user) {
                this.$emit('activeUsers', user)
            },
            bandUsers(user) {
                this.$emit('bandUsers', user)
            },
            resetPasswordUsers(user) {
                this.$emit('resetPasswordUsers', user)
            },
            updateRoleUsers(user) {
                this.$emit('updateRoleUsers', user)
            }
        }
    }
</script>

<style lang="scss" scoped>
.manager-user-list-component {
    .user-list-action {
        margin-right: 0.25rem;
    }
}
</style>

<style lang="scss">
    .manager-user-list-component {
        .user-item-deleted {
            color: rgba(0,0,0,0.5);
            &:hover {
                color: rgba(0,0,0,0.5);
            }
        }
    }
    .manager-user-action-dropdown {
        color: rgba(0,0,0,1);
    }
</style>