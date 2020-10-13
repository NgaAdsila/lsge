<template>
    <div>
        <h4>
            {{ $t('manager.label.member_online') }}
        </h4>
        <b-table
                id="login-history-table"
                hover
                :items="userList"
                :fields="fields"
                head-variant='light'
                responsive
                show-empty
        >
            <template v-slot:empty="">
                <b-alert variant="warning" show>{{ $t('common.message.no_data') }}</b-alert>
            </template>
        </b-table>
    </div>
</template>

<script>
    import {smartTime} from "@/utils";

    export default {
        name: "ManagerDashboardListComponent",
        props: ['onlineUsers'],
        computed: {
            userList: function () {
                return this.onlineUsers.map((v) => {
                    v.lastLoginTitle = ''
                    if (v.lastLogin) {
                        v.lastLoginTitle = smartTime(v.lastLogin * 1000)
                    }
                    return v
                }).sort((a, b) => b.lastLogin - a.lastLogin)
            }
        },
        data() {
            return {
                fields: ['name', {
                    key: 'lastLoginTitle',
                    label: 'Last login'
                }]
            }
        }
    }
</script>

<style scoped>

</style>