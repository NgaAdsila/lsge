<template>
    <div class="manager-user-list-component">
        <b-table
                id="login-history-table"
                hover
                :items="userList"
                :fields="fields"
                :per-page="paging.limit"
                head-variant='light'
                responsive
                show-empty
                :tbody-tr-class="rowClass"
        >
            <template v-slot:empty="">
                <b-alert variant="warning" show>{{ $t('common.message.no_data') }}</b-alert>
            </template>
            <template v-slot:cell(onlineStatus)="data">
                <b v-show="isOnlineUser(data.item.id)">{{$t('manager.label.online.on')}}</b>
                <span v-show="!isOnlineUser(data.item.id)">{{$t('manager.label.online.off')}}</span>
            </template>
            <template v-slot:cell(action)="data">
                <b-dropdown size="sm" variant="link" toggle-class="text-decoration-none" no-caret right>
                    <template v-slot:button-content>
                        <b-icon icon="three-dots" variant="success"></b-icon>
                    </template>
                    <b-dropdown-item href="#">{{$t('manager.action.reset_password')}}</b-dropdown-item>
                    <b-dropdown-item href="#">{{$t('manager.action.update_role')}}</b-dropdown-item>
                    <b-dropdown-item href="#">
                        <span v-if="data.item.status">{{$t('manager.action.active_user')}}</span>
                        <span v-else>{{$t('manager.action.band_user')}}</span>
                    </b-dropdown-item>
                </b-dropdown>
            </template>
        </b-table>
        <b-pagination
                v-if="paging.total / paging.limit > 1"
                v-model="paging.page"
                :total-rows="paging.total"
                :per-page="paging.limit"
                aria-controls="login-history-table"
                first-number
                last-number
                pills
                align="center"
                @change="changePage"
        ></b-pagination>
    </div>
</template>

<script>
    export default {
        name: "ManagerUserListComponent",
        props: [
            'userList',
            'paging',
            'fields',
            'onlineUsers'
        ],
        methods: {
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
            background-color: rgba(0,0,0,0.2) !important;
        }
    }
</style>