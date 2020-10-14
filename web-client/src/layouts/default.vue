<template>
    <div>
        <Header
                :currentUserRole="currentUserRole"
                @searchFriend="searchFriend"
                @logout="logout"/>
        <b-container fluid>
            <router-view :key="$route.fullPath" />
            <FriendList
                    ref="friendList"
                    :friendList="friendList"
                    :requestedFriends="requestedFriends"
                    :onlineUserIds="onlineUserIds"
                    :isLoading="isLoading"
                    :currentUserId="currentUserId"
                    @getFriendList="getFriendList"
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
    import {signOut, refreshToken, getRole} from '@/services/user_service';
    import ConfirmModal from "../components/modal/ConfirmModal";
    import {parseJwt} from "@/utils";
    import {ECHO_CHANNEL, ECHO_EVENT, RESPONSE, VARIANT} from "@/services/constants";
    import FriendList from "../components/home/FriendList";
    import {getFriendList} from "@/services/relationship_service";
    import {initNormalChatroom} from "@/services/chatroom_service";
    import {initEcho} from "@/helper/EchoClientHelper";
    import ToastHelper from "@/helper/ToastHelper";
    import {approveFriend, cancelFriend} from "@/services/relationship_service";
    import {decode} from "@/utils/encrypt";
    import {checkRole} from "@/services/role";
    export default {
        name: "default",
        components: {FriendList, ConfirmModal, Footer, Header},
        data() {
            return {
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
            },
            currentUserRole: function () {
                return this.$store.getters.role
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
                        const res = decode(data.message)
                        if (res.recUserId === this.currentUserId) {
                            this.$store.commit('removeFriendApprovedId', res.reqUserId)
                            this.$store.commit('removeFriendCancelledId', res.reqUserId)
                            this.$store.commit('addFriendRequestedId', res.reqUserId)
                            ToastHelper.notify(this.$t('find-friend.message.notify_has_new_request', { name: res.name }))
                            this.getFriendList();
                        }
                    })
                    .listen(ECHO_EVENT.APPROVE_FRIEND, data => {
                        const res = decode(data.message)
                        if (res.reqUserId === this.currentUserId) {
                            this.$store.commit('removeFriendRequestedId', res.recUserId)
                            this.$store.commit('removeFriendCancelledId', res.recUserId)
                            this.$store.commit('addFriendApprovedId', res.recUserId)
                            ToastHelper.notify(
                                this.$t('find-friend.message.notify_approved_friend', { name: res.name }), VARIANT.SUCCESS)
                        }
                        if (res.reqUserId === this.currentUserId || res.recUserId === this.currentUserId) {
                            this.getFriendList();
                        }
                    })
                    .listen(ECHO_EVENT.CANCEL_FRIEND, data => {
                        const res = decode(data.message)
                        if (res.reqUserId === this.currentUserId) {
                            this.$store.commit('removeFriendRequestedId', res.recUserId)
                            this.$store.commit('removeFriendApprovedId', res.recUserId)
                            this.$store.commit('addFriendCancelledId', res.recUserId)
                            ToastHelper.notify(
                                this.$t('find-friend.message.notify_cancelled_friend', { name: res.name }), VARIANT.DANGER)
                        }
                        if (res.reqUserId === this.currentUserId || res.recUserId === this.currentUserId) {
                            this.getFriendList();
                        }
                    })
                    .listen(ECHO_EVENT.CREATE_MESSAGE, data => {
                        this.handleChangeMessage(decode(data.message))
                    })
                    .listen(ECHO_EVENT.IS_READ_MESSAGE, data => {
                        this.handleChangeMessage(decode(data.message))
                    })
                    .listen(ECHO_EVENT.AUTO_READ, data => {
                        this.handleAutoReadMessage(decode(data.message))
                    })
                    .listen(ECHO_EVENT.BAND_USER, data => {
                        this.handleBandUser(decode(data.message))
                    })
                    .listen(ECHO_EVENT.RESET_PASSWORD_USER, data => {
                        this.handleResetPasswordUser(decode(data.message))
                    })
                    .listen(ECHO_EVENT.UPDATE_ROLE_USER, data => {
                        this.handleUpdateRoleUser(decode(data.message))
                    })
            },
            async approveFriend(user) {
                try {
                    this.isLoading = true;
                    const res = await approveFriend({userId: user.id});
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        ToastHelper.message(
                            this.$t('find-friend.message.approve_friend_success', {name: user.name}))
                        this.forceReloadPage()
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
                        this.forceReloadPage()
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch(e) {
                    console.log('cancel friend error: ', e);
                } finally {
                    this.isLoading = false;
                }
            },
            forceReloadPage() {
                if (this.$route.name === 'FindFriend') {
                    const query = { ...this.$route.query, _: '' + new Date().getTime() }
                    this.$router.replace({ ...this.$route, query })
                }
            },
            handleChangeMessage(message) {
                if (!message || message.statuses.every(s => s.userId !== this.currentUserId) || !this.friendList) {
                    return
                }
                let recUser = message.statuses.find(s => s.userId !== this.currentUserId);
                for (let f of this.friendList) {
                    if (f.id === recUser.userId) {
                        f.lastMessage = message
                        break
                    }
                }
            },
            handleAutoReadMessage(data) {
                if (data.userId !== this.currentUserId || !this.friendList) {
                  return
                }
                for (let f of this.friendList) {
                    if (f.lastMessage && f.lastMessage.chatroomId === data.chatroomId && f.lastMessage.statuses) {
                        f.lastMessage.statuses.forEach(s => {
                            if (s.userId === data.userId) {
                                s.seen = true
                            }
                        })
                        break
                    }
                }
            },
            handleBandUser(data) {
                if (!data.userIds || data.userIds.length === 0 || !data.userIds.includes(this.currentUserId)) {
                    return
                }
                ToastHelper.notify(this.$t('manager.notify.band_user'), VARIANT.DANGER)
                this.onOk()
            },
            handleResetPasswordUser(data) {
                if (!data.userIds || data.userIds.length === 0 || !data.userIds.includes(this.currentUserId)) {
                    return
                }
                ToastHelper.notify(this.$t('manager.notify.reset_password'), VARIANT.DANGER)
                this.onOk()
            },
            async handleUpdateRoleUser(data) {
                if (!data.userIds || data.userIds.length === 0 || !data.userIds.includes(this.currentUserId)) {
                    return
                }
                ToastHelper.notify(this.$t('manager.notify.update_role'), VARIANT.DANGER)
                if (this.$route.path === '/') {
                    return
                }
                try {
                    const res = await getRole()
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        if (!checkRole(this.$route.name, res.data)) {
                            return this.$router.push('/')
                        }
                    }
                } catch (e) {
                    console.log('Get role error: ', e)
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