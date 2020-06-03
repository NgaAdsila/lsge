<template>
    <div class="login-history-filter">
        <div v-if="historyList.length > 0">
            <b-table
                    id="login-history-table"
                    hover
                    :items="historyList"
                    :fields="fields"
                    :per-page="paging.limit"
                    :current-page="paging.page"
                    head-variant='light'
            ></b-table>
            <b-pagination
                    v-model="paging.page"
                    :total-rows="paging.total"
                    :per-page="paging.limit"
                    aria-controls="login-history-table"
                    first-number
                    last-number
                    pills
                    align="center"
            ></b-pagination>
        </div>
        <div v-else>
            <b-alert variant="warning" show>{{ $t('common.message.no_data') }}</b-alert>
        </div>
    </div>
</template>

<script>
    export default {
        name: "LoginHistoryComponent",
        props: [
            'historyList',
            'paging',
            'fields'
        ]
    }
</script>

<style scoped>

</style>