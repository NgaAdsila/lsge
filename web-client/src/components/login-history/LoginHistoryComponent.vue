<template>
    <div class="login-history-filter">
        <b-table
                id="login-history-table"
                hover
                :items="historyList"
                :fields="fields"
                :per-page="paging.limit"
                head-variant='light'
                responsive
                show-empty
        >
            <template v-slot:empty="">
                <b-alert variant="warning" show>{{ $t('common.message.no_data') }}</b-alert>
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
        name: "LoginHistoryComponent",
        props: [
            'historyList',
            'paging',
            'fields'
        ],
        methods: {
            changePage(page) {
                if (page === this.paging.page) {
                    return;
                }
                this.$emit('changePage', page);
            }
        }
    }
</script>

<style scoped>

</style>