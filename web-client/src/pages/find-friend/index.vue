<template>
    <div class="find-friend-page">
        <b-breadcrumb :items="crumbItems"></b-breadcrumb>
        <b-overlay :show="isLoading" rounded="sm" spinner-variant="primary">
            <FindFriendComponent :userList="userList"
                                 @addFriend="addFriend"
                                 @approveFriend="approveFriend"
                                 @cancelFriend="cancelFriend" />
        </b-overlay>
    </div>
</template>

<script>
    import FindFriendComponent from "../../components/find-friend/FindFriendComponent";
    import { addFriend, cancelFriend, approveFriend } from "../../services/relationship_service";
    import { getList } from "../../services/user_service";
    import {RELATION_STATUS, RESPONSE} from "../../services/constants";

    export default {
        name: "FindFriend",
        components: {FindFriendComponent},
        data() {
            return {
                crumbItems: [
                    {
                        text: this.$t('common.label.home'),
                        href: '/home'
                    },
                    {
                        text: this.$t('common.label.find-friend'),
                        active: true
                    }
                ],
                isLoading: false,
                userList: []
            }
        },
        computed: {
            keyword: function () {
                return this.$route.query.keyword;
            }
        },
        watch: {
            keyword: function (value) {
                this.initData(value);
            }
        },
        mounted() {
            this.initData(this.keyword)
        },
        methods: {
            async initData(keyword) {
                if (keyword) {
                    this.crumbItems[1].text = this.$t('common.label.find-friend') + ` with keyword: ${keyword}`
                } else {
                    this.crumbItems[1].text = this.$t('common.label.find-friend')
                }
                try {
                    this.isLoading = true;
                    const res = await getList(keyword);
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.userList = res.data.responses;
                    } else {
                        this.userList = [];
                    }
                } catch (e) {
                    console.log('Get user list error: ', e);
                } finally {
                    this.isLoading = false;
                }
            },
            async addFriend(userId, index) {
                try {
                    this.isLoading = true;
                    const res = await addFriend({recUserId: userId});
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.userList[index].hasFriendReq = true;
                        this.$bvToast.toast(this.$t('common.message.update_success'), {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'success',
                            autoHideDelay: 2000
                        });
                    } else {
                        this.$bvToast.toast(res.message, {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'danger',
                            autoHideDelay: 2000
                        });
                    }
                } catch(e) {
                    console.log('add friend error: ', e);
                } finally {
                    this.isLoading = false;
                }
            },
            async cancelFriend(userId, index) {
                try {
                    this.isLoading = true;
                    const res = await cancelFriend({userId: userId});
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.userList[index].hasFriendReq = false;
                        this.$bvToast.toast(this.$t('common.message.update_success'), {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'success',
                            autoHideDelay: 2000
                        });
                    } else {
                        this.$bvToast.toast(res.message, {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'danger',
                            autoHideDelay: 2000
                        });
                    }
                } catch(e) {
                    console.log('cancel friend error: ', e);
                } finally {
                    this.isLoading = false;
                }
            },
            async approveFriend(userId, index) {
                try {
                    this.isLoading = true;
                    const res = await approveFriend({userId: userId});
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.userList[index].canApprove = false;
                        this.userList[index].status = RELATION_STATUS.APPROVED;
                        this.$bvToast.toast(this.$t('common.message.update_success'), {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'success',
                            autoHideDelay: 2000
                        });
                    } else {
                        this.$bvToast.toast(res.message, {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'danger',
                            autoHideDelay: 2000
                        });
                    }
                } catch(e) {
                    console.log('cancel friend error: ', e);
                } finally {
                    this.isLoading = false;
                }
            }
        }
    }
</script>

<style scoped>

</style>