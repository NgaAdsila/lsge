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
                    :onlineUserIds="onlineUserIds"
                    @createChatRoom="createChatRoom" />
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
                chatWithUserId: null,
                echoConnect: null,
                onlineUserIds: []
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
                    const res = await getFriendList();
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.friendList = res.data.responses;
                    }
                } catch (e) {
                    console.log('Get friend list error: ', e);
                }
            },
            async createChatRoom(user) {
                try {
                    if (this.$route.name === 'ChatDetail' && this.chatWithUserId === user.id) {
                        this.$refs.friendList.$refs.friendListButton.click()
                        return
                    }
                    const res = await initNormalChatroom({ userId: user.id });
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.chatWithUserId = user.id;
                        this.$bvToast.toast(this.$t('chatroom.message.init_normal_success', {name: user.name}), {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'success',
                            autoHideDelay: 2000
                        });
                        setTimeout(async (self = this) => {
                            this.$refs.friendList.$refs.friendListButton.click()
                            await self.$router.push({ name: 'ChatDetail', params: { id: res.data.id } })
                        }, 500);
                    } else {
                        this.$bvToast.toast(res.message, {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'danger',
                            autoHideDelay: 2000
                        });
                    }
                } catch (e) {
                    console.log('Create chatroom error: ', e)
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