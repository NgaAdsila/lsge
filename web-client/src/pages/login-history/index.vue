<template>
    <div class="login-history-page">
        <b-breadcrumb :items="crumbItems"></b-breadcrumb>
        <div class="d-flex justify-content-center mb-3" v-if="!!isLoading">
            <b-spinner label="Loading..." variant="primary"></b-spinner>
        </div>
        <LoginHistoryHeader v-if="!isLoading" />
        <LoginHistoryComponent
                v-if="!isLoading"
                :historyList="historyList"
                :fields="fields"
                :paging="paging"/>
    </div>
</template>

<script>
    import { filterByUser } from '../../services/login_history_service';
    import LoginHistoryComponent from "../../components/login-history/LoginHistoryComponent";
    import { RESPONSE, PAGINATION } from "../../services/constants";
    import LoginHistoryHeader from "../../components/login-history/LoginHistoryHeader";

    export default {
        name: "LoginHistory",
        components: {LoginHistoryHeader, LoginHistoryComponent},
        data() {
            return {
                historyList: [],
                req: {
                    page: PAGINATION.DEFAULT_PAGE,
                    limit: PAGINATION.LIMIT,
                    keyword: null
                },
                paging: {
                    page: PAGINATION.DEFAULT_PAGE,
                    limit: PAGINATION.LIMIT,
                    number: 0,
                    total: 0
                },
                crumbItems: [
                    {
                        text: this.$t('common.label.home'),
                        href: '/home'
                    },
                    {
                        text: this.$t('common.label.login_history'),
                        active: true
                    }
                ],
                isLoading: true,
                fields: ['no', 'loginTime', 'ipAddress']
            }
        },
        async mounted() {
            await this.getList();
        },
        methods: {
            async getList() {
                try {
                    const res = await filterByUser(this.req);
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        let histories = [];
                        let index = 1;
                        for (let item of res.data.responses) {
                            histories.push({
                                no: (res.data.paging.page - 1) * res.data.paging.limit + index++,
                                loginTime: item.createdAt,
                                ipAddress: item.ipAddress
                            })
                        }
                        this.historyList = histories;
                        this.paging = res.data.paging;
                    } else {
                        console.log('Filter error: ', res.message);
                    }
                } catch (e) {
                    console.log('Filter Error: ', e);
                } finally {
                    this.isLoading = false;
                }
            }
        }
    }
</script>

<style scoped>

</style>