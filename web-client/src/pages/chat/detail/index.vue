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
                    @createMessage="createMessage"
            />
        </b-overlay>
    </div>
</template>

<script>
    import ChatDetailComponent from "../../../components/chat/detail/ChatDetailComponent";
    import { findById, createMessage, isReadMessage } from "@/services/chatroom_service";
    import {RESPONSE, VARIANT} from "@/services/constants";
    import {initEcho} from "../../../helper/EchoClientHelper";
    import ToastHelper from "@/helper/ToastHelper";

    export default {
        name: "ChatDetail",
        components: {ChatDetailComponent},
        computed: {
            chatroomId: function () {
                return this.$route.params.id
            },
            currentUserId: function () {
                return this.$store.getters.id
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
                        text: this.$t('common.label.chat_list'),
                        to: { name: 'ChatList' }
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
                            this.chatroomName = this.crumbItems[2].text = res.data.name
                        }
                        this.messages = res.data.messages;
                        if (res.data.users.length > 0) {
                            const users = [];
                            res.data.users.forEach(u => users[u.id] = u);
                            this.users = users
                        }
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
            async handleChannelMessage() {
                this.echoConnect = await initEcho();
                this.echoConnect.join(this.channelName)
                    .joining(user => {
                        this.handleJoiningMessage(user)
                    })
                    .listen('created-message', (data) => {
                        this.handleCreatedMessage(data.message)
                    })
                    .listen('is-read-message', (data) => {
                        this.handleIsReadMessage(data.message)
                    })
            },
            async createMessage(message) {
                try {
                    this.isSubmitting = true
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
            }
        }
    }
</script>

<style scoped>

</style>