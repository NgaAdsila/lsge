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
    import { findById, createMessage } from "@/services/chatroom_service";
    import {RESPONSE} from "@/services/constants";

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
                        href: '/home'
                    },
                    {
                        text: this.$t('common.label.chat_list'),
                        href: '/chat-list'
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
                isSubmitting: false
            }
        },
        created() {
            this.initData();
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
                        this.$bvToast.toast(res.message, {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'danger',
                            autoHideDelay: 2000
                        });
                        setTimeout(async (self = this) => {
                            await self.$router.push({ name: 'ChatList' });
                        }, 2000);
                    }
                } catch (e) {
                    console.log('Get chatroom error: ', e)
                } finally {
                    this.isLoading = false
                }
            },
            async createMessage(message) {
                try {
                    this.isSubmitting = true
                    const res = await createMessage({
                        chatroomId: this.chatroomId,
                        message: message
                    });
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.messages.push(res.data)
                        console.log(this.messages);
                    } else {
                        console.log('ERRRR: ', res.message)
                    }
                    return true;
                } catch (e) {
                    console.log('Create message error: ', e)
                } finally {
                    this.isSubmitting = false
                }
                return false;
            }
        }
    }
</script>

<style scoped>

</style>