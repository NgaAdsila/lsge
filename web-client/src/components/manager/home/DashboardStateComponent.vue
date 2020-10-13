<template>
    <div class="manager-dashboard-state-component">
        <b-row>
            <b-col sm="6" lg="3"
                   class="mb-2"
                   v-for="(v, index) in ['primary', 'secondary', 'success', 'info']"
                   :key="index">
                <b-card :bg-variant="v" text-variant="white"
                        :title="'' + onlineUserCount">
                    <b-card-text>{{ $t('manager.label.member_online') }}</b-card-text>
                </b-card>
            </b-col>
        </b-row>
    </div>
</template>

<script>
export default {
    name: "ManagerDashboardStateComponent",
    props: ['onlineUsers'],
    computed: {
        onlineUserCount: function () {
            return this.onlineUsers ? this.onlineUsers.length : 0
        }
    }
}
</script>

<style scoped>

</style>