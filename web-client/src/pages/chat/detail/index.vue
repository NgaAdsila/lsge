<template>
    <div class="chat-detail-page">
        <b-breadcrumb :items="crumbItems"></b-breadcrumb>
        <b-overlay :show="isLoading" rounded="sm" spinner-variant="primary">
            <ChatDetailComponent
                    :chatroomName="chatroomName"
                    :messages="messages"
                    :users="users"
                    :currentUserId="currentUserId"
                    :isSubmitting="isSubmitting"
                    :currentUserColor="currentUserColor"
                    :currentUserAvatar="currentUserAvatar"
                    @createMessage="createMessage"
                    @updateChatroom="updateChatroom"
                    @setNickname="setNickname"
            />
        </b-overlay>
    </div>
</template>

<script>
    import ChatDetailComponent from "../../../components/chat/detail/ChatDetailComponent";
    import {
      findById, createMessage, isReadMessage,
      updateChatroom, setNickname, pushAutoReadMessageEvent
    } from "@/services/chatroom_service";
    import {ECHO_EVENT, RESPONSE, VARIANT} from "@/services/constants";
    import {initEcho} from "@/helper/EchoClientHelper";
    import ToastHelper from "@/helper/ToastHelper";
    import {decode} from "@/utils/encrypt";

    export default {
        name: "ChatDetail",
        components: {ChatDetailComponent},
        computed: {
            chatroomId: function () {
                return this.$route.params.id
            },
            currentUserId: function () {
                return this.$store.getters.id
            },
            currentUserColor: function () {
                return this.$store.getters.color || '#007bff'
            },
            currentUserAvatar: function () {
                return this.$store.getters.avatar
            }
        },
        data() {
            return {
                crumbItems: [
                    {
                        text: this.$t('common.label.home'),
                        to: { name: 'Home' }
                    },
                    {
                        text: this.$t('common.label.chat_detail'),
                        active: true
                    }
                ],
                isLoading: false,
                chatroomName: '',
                messages: [],
                users: [],
                isSubmitting: false,
                echoConnect: null,
                channelName: `channel-message-${this.$route.params.id}`
            }
        },
        created() {
            this.initData();
        },
        beforeDestroy() {
            if (this.echoConnect) {
                this.echoConnect.leave(this.channelName);
                this.echoConnect = null;
            }
        },
        methods: {
            async initData() {
                try {
                    this.isLoading = true;
                    const res = await findById(this.chatroomId);
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        if (res.data.name) {
                            this.chatroomName = this.crumbItems[1].text = res.data.name
                        }
                        this.messages = res.data.messages || []
                        this.users = res.data.users || []
                        this.pushAutoReadMessage()
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                        setTimeout(async (self = this) => {
                            await self.$router.push({ name: 'ChatList' });
                        }, 2000);
                    }
                } catch (e) {
                    console.log('Get chatroom error: ', e)
                } finally {
                    this.isLoading = false
                }
                await this.handleChannelMessage()
            },
            pushAutoReadMessage() {
                try {
                    pushAutoReadMessageEvent({
                        chatroomId: this.chatroomId
                    })
                } catch (e) {
                    console.log('Push auto read message event error: ', e)
                }
            },
            async handleChannelMessage() {
                this.echoConnect = await initEcho();
                this.echoConnect.join(this.channelName)
                    .joining(user => {
                        this.handleJoiningMessage(user)
                    })
                    .listen(ECHO_EVENT.MESSAGE, (data) => {
                        this.handleChatEvent(decode(data.message))
                    })
            },
            handleChatEvent(res) {
                if (!res || !res.hasOwnProperty("type") || !res.hasOwnProperty("data")) {
                    return
                }
                switch (res.type) {
                  case ECHO_EVENT.CREATE_MESSAGE:
                      return this.handleCreatedMessage(res.data)
                  case ECHO_EVENT.IS_READ_MESSAGE:
                      return this.handleIsReadMessage(res.data)
                  case ECHO_EVENT.UPDATE_CHATROOM:
                      return this.handleUpdateChatroom(res.data)
                  case ECHO_EVENT.SET_NICKNAME:
                      return this.handleSetNickname(res.data)
                  default:
                    return
                }
            },
            async createMessage(message) {
                try {
                    this.isSubmitting = true
                    if (!message || message.trim() === '') {
                        return true
                    }
                    await createMessage({
                        chatroomId: this.chatroomId,
                        message: message
                    });
                    return true
                } catch (e) {
                    console.log('Create message error: ', e)
                } finally {
                    this.isSubmitting = false
                }
                return false
            },
            async handleCreatedMessage(message) {
                try {
                    this.messages.push(message);
                    if (message.createdBy === this.currentUserId) {
                        return
                    }
                    const res = await isReadMessage(message.id)
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        message.statuses.map(s => {
                            if (s.userId === this.currentUserId) {
                                s.seen = true
                            }
                            return s
                        })
                    }
                } catch (e) {
                    console.log('Is read message error: ', e);
                }
            },
            handleIsReadMessage(message) {
                let userIds = []
                message.statuses.forEach(s => {
                    if (s.userId !== this.currentUserId && s.seen) {
                        userIds.push(s.userId)
                    }
                })
                if (userIds.length <= 0) {
                    return
                }
                this.messages.forEach(m => {
                    m.statuses.forEach(s => {
                        if (!s.seen && userIds.includes(s.userId)) {
                            s.seen = true
                        }
                    })
                })
            },
            handleJoiningMessage(user) {
                this.messages.forEach(m => {
                    if (m.createdBy === user.id) {
                        return
                    }
                    m.statuses.forEach(s => {
                        if (!s.seen && s.userId === user.id) {
                            s.seen = true
                        }
                    })
                })
            },
            async updateChatroom(name) {
                try {
                    const res = await updateChatroom({
                        id: this.chatroomId,
                        name: name
                    })
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.chatroomName = name
                        ToastHelper.message(this.$t('common.message.update_success'))
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch (e) {
                    console.log('update chatroom error: ', e)
                }
            },
            async setNickname(users) {
                try {
                    const res = await setNickname({
                        id: this.chatroomId,
                        userList: users
                    })
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.users = users
                        ToastHelper.message(this.$t('common.message.update_success'))
                    } else {
                        ToastHelper.message(res.message, VARIANT.DANGER)
                    }
                } catch (e) {
                    console.log('set nickname error: ', e)
                }
            },
            handleUpdateChatroom(data) {
                this.chatroomName = data.chatroomName
                this.crumbItems[1].text = data.chatroomName || this.$t('common.label.chat_detail')
                if (data.message) {
                    this.handleCreatedMessage(data.message)
                }
            },
            handleSetNickname(data) {
                if (data.userList && data.userList.length) {
                    this.users = data.userList
                }
                if (data.message) {
                    this.handleCreatedMessage(data.message)
                }
            }
        }
    }
</script>

<style scoped>

</style>