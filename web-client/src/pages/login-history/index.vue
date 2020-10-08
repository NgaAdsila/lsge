<template>
    <div class="login-history-page">
        <b-breadcrumb :items="crumbItems"></b-breadcrumb>
        <b-overlay :show="isLoading" rounded="sm" spinner-variant="primary">
            <LoginHistoryHeader
                    :req="req"
                    :paging="paging"
                    @resetReq="resetReq"
                    @search="search" />
            <LoginHistoryComponent
                    :historyList="historyList"
                    :fields="fields"
                    :paging="paging"
                    @changePage="getList"/>
        </b-overlay>
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
                    keyword: null,
                    timeFrom: null,
                    timeTo: null
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
                        to: { name: 'Home' }
                    },
                    {
                        text: this.$t('common.label.login_history'),
                        active: true
                    }
                ],
                isLoading: true,
                fields: ['no', 'loginTime', 'browser', 'ipAddress']
            }
        },
        async mounted() {
            await this.getList();
        },
        methods: {
            async getList(page = PAGINATION.DEFAULT_PAGE) {
                try {
                    this.isLoading = true;
                    const res = await filterByUser({...this.req, ...{page: page || this.paging.page}});
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        let histories = [];
                        let index = (res.data.paging.page - 1) * res.data.paging.limit;
                        for (let item of res.data.responses) {
                            histories.push({
                                no: ++index,
                                loginTime: item.createdAt,
                                browser: item.browser,
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
            },
            async search(searchReq = {}) {
                this.req = {...this.req, ...searchReq};
                await this.getList(PAGINATION.DEFAULT_PAGE);
            },
            resetReq() {
                this.req.keyword = null;
                this.req.timeFrom = null;
                this.req.timeTo = null;
            }
        }
    }
</script>

<style scoped>

</style>