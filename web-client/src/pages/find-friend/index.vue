<template>
    <div class="find-friend-page">
        <b-breadcrumb :items="crumbItems"></b-breadcrumb>
        <b-overlay :show="isLoading" rounded="sm" spinner-variant="primary">
            <FindFriendComponent
                    :userList="userList"
                    @addFriend="addFriend"
                    @approveFriend="approveFriend"
                    @cancelFriend="cancelFriend"
                    @createChatRoom="createChatRoom"
            />
        </b-overlay>
    </div>
</template>

<script>
    import FindFriendComponent from "../../components/find-friend/FindFriendComponent";
    import { addFriend, cancelFriend, approveFriend } from "@/services/relationship_service";
    import { getList } from "@/services/user_service";
    import { initNormalChatroom } from "@/services/chatroom_service";
    import {RELATION_STATUS, RESPONSE, VARIANT} from "@/services/constants";
    import {mapGetters} from "vuex";
    import ToastHelper from "@/helper/ToastHelper";

    export default {
        name: "FindFriend",
        components: {FindFriendComponent},
        data() {
            return {
                crumbItems: [
                    {
                        text: this.$t('common.label.home'),
                        to: { name: 'Home' }
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
            },
            ...mapGetters([
                'friendRequestedIds',
                'friendApprovedIds',
                'friendCancelledIds'
            ])
        },
        watch: {
            keyword: function (value) {
                this.initData(value);
            },
            friendRequestedIds: function () {
                if (!this.userList || this.userList.length <= 0) {
                    return
                }
                this.userList.map(u => {
                    if (this.friendRequestedIds.includes(u.id)) {
                        u.hasFriendReq = true
                        u.canApprove = true
                        u.status = RELATION_STATUS.PENDING
                    }
                    return u
                })
            },
            friendApprovedIds: function () {
                if (!this.userList || this.userList.length <= 0) {
                    return
                }
                this.userList.map(u => {
                    if (this.friendApprovedIds.includes(u.id)) {
                        u.hasFriendReq = true
                        u.canApprove = false
                        u.status = RELATION_STATUS.APPROVED
                    }
                    return u
                })
            },
            friendCancelledIds: function () {
                if (!this.userList || this.userList.length <= 0) {
                    return
                }
                this.userList.map(u => {
                    if (this.friendCancelledIds.includes(u.id)) {
                        u.hasFriendReq = false
                        u.canApprove = false
                        u.status = RELATION_STATUS.CANCEL
                    }
                    return u
                })
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
                        this.userList[index].canApprove = false;
                        this.userList[index].status = RELATION_STATUS.PENDING;
                        ToastHelper.message(this.$t('find-friend.message.add_friend_success'))
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
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
                        this.userList[index].canApprove = false;
                        this.userList[index].status = RELATION_STATUS.CANCEL;
                        ToastHelper.message(this.$t('find-friend.message.cancel_friend_success'))
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
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
                        this.userList[index].hasFriendReq = true;
                        this.userList[index].canApprove = false;
                        this.userList[index].status = RELATION_STATUS.APPROVED;
                        ToastHelper.message(
                            this.$t('find-friend.message.approve_friend_success', {name: this.userList[index].name}))
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch(e) {
                    console.log('cancel friend error: ', e);
                } finally {
                    this.isLoading = false;
                }
            },
            async createChatRoom(userId, index) {
                try {
                    this.isLoading = true;
                    const res = await initNormalChatroom({userId: userId});
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        ToastHelper.message(
                            this.$t('chatroom.message.init_normal_success', {name: this.userList[index].name}))
                        setTimeout(async (self = this) => {
                            await self.$router.push({ name: 'ChatDetail', params: { id: res.data.id } });
                            this.isLoading = false;
                        }, 500);
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                        this.isLoading = false;
                    }
                } catch(e) {
                    console.log('create chatroom error: ', e);
                    this.isLoading = false;
                }
            }
        }
    }
</script>

<style scoped>

</style>