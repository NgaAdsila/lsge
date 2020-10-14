<template>
    <div class="manage-user-page">
        <b-breadcrumb :items="crumbItems"></b-breadcrumb>
        <b-overlay :show="isLoading" rounded="sm" spinner-variant="primary">
            <ManagerUserListHeaderComponent
                    :req="req"
                    :paging="paging"
                    :roleOptions="roleOptions"
                    :selectedRows="selectedRows"
                    @resetReq="resetReq"
                    @search="search"
                    @activeUsers="activeUsers"
                    @bandUsers="bandUsers"
                    @resetPasswordUsers="resetPasswordUsers"
                    @updateRoleUsers="updateRoleUsers"
            />
            <ManagerUserListComponent
                    ref="managerUserList"
                    :userList="userList"
                    :paging="paging"
                    :fields="fields"
                    :onlineUsers="onlineUsers"
                    :selectedAll="selectedAll"
                    @changePage="getUserList"
                    @onRowSelected="onRowSelected"
                    @activeUsers="activeUsers"
                    @bandUsers="bandUsers"
                    @resetPasswordUsers="resetPasswordUsers"
                    @updateRoleUsers="updateRoleUsers"
            />
        </b-overlay>
        <ConfirmModal
            :message="confirmMessage"
            :data="confirmData"
            @onOk="confirmOK" />
        <ManagerUserListUpdateRole
            :users="confirmData.users"
            :roleOptions="roleOptions"
            @onOk="submitUpdateRoleUsers" />
    </div>
</template>

<script>
import {fail, PAGINATION, SELECTED_MODE, VARIANT} from "@/services/constants";
import {activeUsers, bandUsers, filter, resetPasswordUsers, updateRoleUsers} from "@/services/user_service";
import {RESPONSE} from "@/services/constants";
import ManagerUserListComponent from "../../../components/manager/user/UserListComponent";
import ManagerUserListHeaderComponent from "../../../components/manager/user/UserListHeaderComponent";
import {getRoleOptions} from "@/services/user_service";
import ConfirmModal from "@/components/modal/ConfirmModal";
import ToastHelper from "@/helper/ToastHelper";
import ManagerUserListUpdateRole from "@/components/manager/user/UserListUpdateRole";

export default {
    name: "ManagerUser",
    components: {ManagerUserListUpdateRole, ConfirmModal, ManagerUserListHeaderComponent, ManagerUserListComponent},
    props: ['onlineUsers'],
    data() {
        return {
            userList: [],
            roleOptions: [],
            req: {
                page: PAGINATION.DEFAULT_PAGE,
                limit: PAGINATION.LIMIT,
                keyword: null,
                status: null,
                roles: []
            },
            paging: {
                page: PAGINATION.DEFAULT_PAGE,
                limit: PAGINATION.LIMIT,
                number: 0,
                total: 0
            },
            crumbItems: [
                {
                    text: this.$t('manager.label.dashboard'),
                    to: { name: 'ManagerHome' }
                },
                {
                    text: this.$t('manager.label.user_list'),
                    active: true
                }
            ],
            isLoading: true,
            selectedRows: [],
            selectedAll: 'none',
            fields: [{
              key: 'selected',
              label: ''
            }, 'no', 'username', 'fullName', 'email', 'roles', 'onlineStatus', 'createdAt', 'action'],
            confirmMessage: '',
            confirmData: {
                action: null,
                users: []
            }
        }
    },
    mounted() {
        this.initData()
    },
    methods: {
        initData() {
            this.resetReq()
            this.$refs.managerUserList.$refs.managerUserTable.clearSelected()
            this.confirmMessage = ''
            this.confirmData = {
                action: null,
                users: []
            }
            this.getUserList()
            this.getRoleOptions()
        },
        async getUserList(page = PAGINATION.DEFAULT_PAGE) {
            try {
                this.isLoading = true
                this.selectedAll = SELECTED_MODE.NONE
                const res = await filter({...this.req, ...{page: page || this.paging.page}})
                if (res.status === RESPONSE.STATUS.SUCCESS) {
                    let userList = []
                    let index = (res.data.paging.page - 1) * res.data.paging.limit;
                    for (let item of res.data.responses) {
                        userList.push({
                            no: ++index,
                            id: item.id,
                            username: item.username,
                            fullName: item.name,
                            email: item.email,
                            status: item.deleted,
                            roles: item.roles,
                            createdAt: item.createdAt
                        })
                    }
                    this.userList = userList
                    this.paging = res.data.paging;
                } else {
                    console.log('Filter error: ', res.message);
                }
            } catch (e) {
                console.log('Filter Error: ', e);
            } finally {
                this.isLoading = false;
            }
        },
        async getRoleOptions() {
            try {
                const res = await getRoleOptions();
                if (res.status === RESPONSE.STATUS.SUCCESS) {
                    this.roleOptions = res.data.responses;
                } else {
                    console.log('Get role option error: ', res.message)
                }
            } catch (e) {
                console.log('Get role option error: ', e)
            }
        },
        async search(searchReq = {}) {
            this.req = {...this.req, ...searchReq};
            await this.getUserList(PAGINATION.DEFAULT_PAGE);
        },
        resetReq() {
            this.req.keyword = null;
            this.req.status = null;
            this.req.roles = [];
        },
        onRowSelected(items) {
            this.selectedRows = items
            this.selectedAll = items.length === 0
                ? SELECTED_MODE.NONE
                : (items.length === this.userList.length ? SELECTED_MODE.ALL : SELECTED_MODE.SOME);
        },
        activeUsers(users) {
            users = Array.isArray(users) ? users : [users]
            this.confirmMessage = this.$t('manager.modal.message.active_user', {
                accounts: '[' + users.map(u => u.username).join('], [') + ']'
            })
            this.confirmData.action = 'active'
            this.confirmData.users = users
            this.$bvModal.show('modal-confirm')
        },
        bandUsers(users) {
            users = Array.isArray(users) ? users : [users]
            this.confirmMessage = this.$t('manager.modal.message.band_user', {
              accounts: '[' + users.map(u => u.username).join('], [') + ']'
            })
            this.confirmData.action = 'band'
            this.confirmData.users = users
            this.$bvModal.show('modal-confirm')
        },
        resetPasswordUsers(users) {
            users = Array.isArray(users) ? users : [users]
            this.confirmMessage = this.$t('manager.modal.message.reset_password', {
              accounts: '[' + users.map(u => u.username).join('], [') + ']'
            })
            this.confirmData.action = 'reset_password'
            this.confirmData.users = users
            this.$bvModal.show('modal-confirm')
        },
        async confirmOK(data) {
            if (data.users.length === 0) {
              return
            }
            try {
                this.isLoading = true
                const res = await this.callApiByAction(data.action, data.users)
                if (res.status === RESPONSE.STATUS.SUCCESS) {
                    ToastHelper.message(this.getMessageSuccess(data.action, res.data));
                    this.initData()
                } else {
                    ToastHelper.message(res.message, VARIANT.DANGER)
                }
            } catch (e) {
                console.log(data.action + ' error: ', e)
            } finally {
                this.isLoading = false
            }
        },
        async callApiByAction(action, users) {
            switch (action) {
                case 'active':
                    return await activeUsers({ userIds: users.map(u => u.id) });
                case 'band':
                    return await bandUsers({ userIds: users.map(u => u.id) });
                case 'reset_password':
                    return await resetPasswordUsers({ userIds: users.map(u => u.id) });
                case 'update_role':
                    return await updateRoleUsers({
                        userRoleList: users.map(u => {
                            return { id: u.id, roles: u.roles }
                        })
                    });
                default:
                    return fail(this.$t('common.validation.request_invalid'), RESPONSE.CODE.BAD_REQUEST)
            }
        },
        getMessageSuccess(action, number) {
            switch (action) {
                case 'active':
                    return this.$t('manager.modal.message.success.active_user', { number: number });
                case 'band':
                    return this.$t('manager.modal.message.success.band_user', { number: number });
                case 'reset_password':
                    return this.$t('manager.modal.message.success.reset_password', { number: number });
                case 'update_role':
                    return this.$t('manager.modal.message.success.update_role', { number: number });
                default:
                    return this.$t('common.message.update_success')
            }
        },
        updateRoleUsers(users) {
          users = Array.isArray(users) ? users : [users]
          this.confirmData.action = 'update_role'
          this.confirmData.users = users
          this.$bvModal.show('manager-user-list-update-role')
        },
        async submitUpdateRoleUsers(users) {
            await this.confirmOK({
                action: this.confirmData.action,
                users: users
            })
        }
    }
}
</script>

<style scoped>

</style>