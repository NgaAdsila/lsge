<template>
    <div>
        <Header
                :user="user"
                @searchFriend="searchFriend"
                @logout="logout"/>
        <b-container fluid>
            <router-view :key="$route.path" />
            <FriendList
                    ref="friendList"
                    :friendList="friendList"
                    :requestedFriends="requestedFriends"
                    :onlineUserIds="onlineUserIds"
                    :isLoading="isLoading"
                    @createChatRoom="createChatRoom"
                    @approveFriend="approveFriend"
                    @cancelFriend="cancelFriend" />
        </b-container>
        <Footer />
        <ConfirmModal :message="confirmMessage" @onOk="onOk" />
    </div>
</template>

<script>
    import Header from "../components/Header";
    import Footer from "../components/Footer";
    import { signOut, refreshToken } from '@/services/user_service';
    import ConfirmModal from "../components/modal/ConfirmModal";
    import {parseJwt} from "@/utils";
    import {ECHO_CHANNEL, ECHO_EVENT, RESPONSE, VARIANT} from "@/services/constants";
    import FriendList from "../components/home/FriendList";
    import {getFriendList} from "@/services/relationship_service";
    import {initNormalChatroom} from "@/services/chatroom_service";
    import {initEcho} from "@/helper/EchoClientHelper";
    import ToastHelper from "@/helper/ToastHelper";
    import {approveFriend, cancelFriend} from "../services/relationship_service";
    export default {
        name: "default",
        components: {FriendList, ConfirmModal, Footer, Header},
        data() {
            return {
                user: {
                    name: this.$store.getters.name
                },
                confirmMessage: '',
                refreshToken: undefined,
                friendList: [],
                requestedFriends: [],
                chatWithUserId: null,
                echoConnect: null,
                onlineUserIds: [],
                isLoading: false
            }
        },
        computed: {
            currentUserId: function () {
                return this.$store.getters.id
            }
        },
        mounted() {
            this.refreshToken = setInterval(async (self = this) => {
                if (self.$store.getters.jwt && parseJwt(self.$store.getters.jwt).exp < Date.now() / 1000) {
                    const res = await refreshToken();
                    if (res.status === RESPONSE.STATUS.ERROR) {
                        await self.logout();
                    }
                }
            }, 1000)
            this.getFriendList()
            this.registerEchoConnection()
        },
        methods: {
            async logout() {
                this.confirmMessage = this.$t('logout.message.question');
                this.$bvModal.show('modal-confirm');
            },
            async searchFriend(keyword = '') {
                await this.$router.push({ name: 'FindFriend', query: { keyword: keyword.trim() } });
            },
            async onOk() {
                this.$bvModal.hide('modal-confirm');
                await signOut();
                await this.$router.push({path: '/login'});
            },
            async getFriendList() {
                try {
                    this.isLoading = true
                    const res = await getFriendList()
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.friendList = res.data.friends
                        this.requestedFriends = res.data.requestedFriends
                    }
                } catch (e) {
                    console.log('Get friend list error: ', e)
                } finally {
                    this.isLoading = false
                }
            },
            async createChatRoom(user) {
                try {
                    this.isLoading = true
                    if (this.$route.name === 'ChatDetail' && this.chatWithUserId === user.id) {
                        this.$refs.friendList.$refs.friendListButton.click()
                        this.isLoading = false
                        return
                    }
                    const res = await initNormalChatroom({ userId: user.id });
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.chatWithUserId = user.id;
                        ToastHelper.message(this.$t('chatroom.message.init_normal_success', {name: user.name}))
                        setTimeout(async (self = this) => {
                            this.$refs.friendList.$refs.friendListButton.click()
                            await self.$router.push({ name: 'ChatDetail', params: { id: res.data.id } })
                        }, 500);
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch (e) {
                    console.log('Create chatroom error: ', e)
                } finally {
                    this.isLoading = false
                }
            },
            async registerEchoConnection() {
                if (!this.echoConnect) {
                    this.echoConnect = await initEcho();
                }
                this.echoConnect.join(ECHO_CHANNEL.CHANNEL_MAIN)
                    .here(users => {
                        this.onlineUserIds = users.map(u => u.id)
                    })
                    .joining(user => {
                        this.onlineUserIds.push(user.id)
                    })
                    .leaving(user => {
                        const index = this.onlineUserIds.findIndex(id => id === user.id)
                        if (index > -1) {
                            this.onlineUserIds.splice(index, 1);
                        }
                    })
                    .listen(ECHO_EVENT.ADD_FRIEND, data => {
                        if (data.recUserId === this.currentUserId) {
                            this.$store.commit('removeFriendApprovedId', data.reqUserId)
                            this.$store.commit('removeFriendCancelledId', data.reqUserId)
                            this.$store.commit('addFriendRequestedId', data.reqUserId)
                            ToastHelper.notify(this.$t('find-friend.message.notify_has_new_request', { name: data.name }))
                            this.getFriendList();
                        }
                    })
                    .listen(ECHO_EVENT.APPROVE_FRIEND, data => {
                        if (data.reqUserId === this.currentUserId) {
                            this.$store.commit('removeFriendRequestedId', data.recUserId)
                            this.$store.commit('removeFriendCancelledId', data.recUserId)
                            this.$store.commit('addFriendApprovedId', data.recUserId)
                            ToastHelper.notify(
                                this.$t('find-friend.message.notify_approved_friend', { name: data.name }), VARIANT.SUCCESS)
                        }
                        if (data.reqUserId === this.currentUserId || data.recUserId === this.currentUserId) {
                            this.getFriendList();
                        }
                    })
                    .listen(ECHO_EVENT.CANCEL_FRIEND, data => {
                        if (data.reqUserId === this.currentUserId) {
                            this.$store.commit('removeFriendRequestedId', data.recUserId)
                            this.$store.commit('removeFriendApprovedId', data.recUserId)
                            this.$store.commit('addFriendCancelledId', data.recUserId)
                            ToastHelper.notify(
                                this.$t('find-friend.message.notify_cancelled_friend', { name: data.name }), VARIANT.DANGER)
                        }
                        if (data.reqUserId === this.currentUserId || data.recUserId === this.currentUserId) {
                            this.getFriendList();
                        }
                    })
            },
            async approveFriend(user) {
                try {
                    this.isLoading = true;
                    const res = await approveFriend({userId: user.id});
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        ToastHelper.message(
                            this.$t('find-friend.message.approve_friend_success', {name: user.name}))
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch(e) {
                    console.log('cancel friend error: ', e);
                } finally {
                    this.isLoading = false;
                }
            },
            async cancelFriend(user) {
                try {
                    this.isLoading = true;
                    const res = await cancelFriend({userId: user.id});
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        ToastHelper.message(this.$t('find-friend.message.cancel_friend_success'))
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch(e) {
                    console.log('cancel friend error: ', e);
                } finally {
                    this.isLoading = false;
                }
            }
        },
        beforeDestroy() {
            if (!this.$store.getters.isLogin) {
                clearInterval(this.refreshToken);
            }
            if (this.echoConnect) {
                this.echoConnect.leave(ECHO_CHANNEL.CHANNEL_MAIN);
                this.echoConnect = null;
            }
        }
    }
</script>

<style scoped>

</style>