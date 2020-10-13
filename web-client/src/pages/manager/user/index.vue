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
                    @search="search" />
            <ManagerUserListComponent
                    :userList="userList"
                    :paging="paging"
                    :fields="fields"
                    :onlineUsers="onlineUsers"
                    :selectedAll="selectedAll"
                    @changePage="getUserList"
                    @onRowSelected="onRowSelected"
            />
        </b-overlay>
    </div>
</template>

<script>
import {PAGINATION, SELECTED_MODE} from "@/services/constants";
    import {filter} from "@/services/user_service";
    import {RESPONSE} from "@/services/constants";
    import ManagerUserListComponent from "../../../components/manager/user/UserListComponent";
    import ManagerUserListHeaderComponent from "../../../components/manager/user/UserListHeaderComponent";
    import {getRoleOptions} from "../../../services/user_service";

    export default {
        name: "ManagerUser",
        components: {ManagerUserListHeaderComponent, ManagerUserListComponent},
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
                }, 'no', 'username', 'fullName', 'email', 'roles', 'onlineStatus', 'createdAt', 'action']
            }
        },
        computed: {
        },
        mounted() {
            this.getUserList()
            this.getRoleOptions()
        },
        methods: {
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
            }
        }
    }
</script>

<style scoped>

</style>