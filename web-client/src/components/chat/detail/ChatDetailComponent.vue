<template>
    <div class="chat-detail-component">
        <div class="chat-detail-header">

        </div>
        <div class="chat-detail-list" ref="chatDetailList">
            <div v-if="messages.length > 0">
                <div v-for="(message, index) in messages" :key="message.id"
                     :class="message.createdBy === currentUserId ? 'is-right' : 'is-left'">
                    <div v-if="message.createdBy === currentUserId" class="chat-detail-item chat-detail-list-right"
                         :class="{'is-group-message': isGroupMessage(message, index) }">
                        <div class="message-content text-break">
                            {{ message.message }}
                        </div>
                        <span class="message-time">{{ displayTime(message.createdAt) }}</span>
                    </div>
                    <div v-else class="chat-detail-item chat-detail-list-left"
                         :class="{'is-group-message': isGroupMessage(message, index) }">
                        <span class="message-user-avatar" :title="displayUserName(message.createdBy)">
                            <b-avatar class="text-uppercase"
                                      size="25"
                                      :text="displayUserName(message.createdBy).charAt(0)"></b-avatar>
                        </span>
                        <div class="message-content text-break">
                            {{ message.message }}
                        </div>
                        <span class="message-time">{{ displayTime(message.createdAt) }}</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="chat-detail-chat-box">
            <b-form-input
                    id="input-message"
                    v-model="newMessage"
                    type="text"
                    required
                    placeholder="Enter message"
                    max="2000"
                    @keypress.enter.exact="createMessage"
            ></b-form-input>
            <b-button class="create-message-button"
                      type="submit" variant="light"
                      @click="createMessage"
                      :disabled="this.newMessage.trim() === '' || this.isSubmitting"
            >
                <b-icon icon="cursor-fill" rotate="45"
                        :variant="this.newMessage.trim() === '' || this.isSubmitting ? 'info' : 'primary'"
                        scale="1.25"></b-icon>
            </b-button>
        </div>
    </div>
</template>

<script>
    import { smartTime } from "@/utils";

    export default {
        name: "ChatDetailComponent",
        props: [
            'chatroomName',
            'currentUserId',
            'messages',
            'users',
            'isSubmitting'
        ],
        data() {
            return {
                newMessage: ''
            }
        },
        updated() {
            this.$refs.chatDetailList.scrollTop = this.$refs.chatDetailList.scrollHeight;
        },
        methods: {
            displayUserName(userId) {
                if (this.users.length <= 0 || !this.users[userId]) {
                    return this.$t('common.label.someone');
                }
                return this.users[userId].nickname || this.users[userId].name;
            },
            displayTime(time) {
                return smartTime(time);
            },
            async createMessage() {
                await this.$emit('createMessage', this.newMessage);
                this.newMessage = '';
            },
            isGroupMessage(message, index) {
                const nextMessage = this.messages[index + 1];
                return nextMessage && message.createdBy === nextMessage.createdBy
                    && nextMessage.createdAt - message.createdAt < 60000;
            }
        }
    }
</script>

<style lang="scss" scoped>
.chat-detail-component {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    height: calc(100vh - 17rem);

    .chat-detail-list {
        height: calc(100% - 4.75rem);
        overflow: auto;

        &::-webkit-scrollbar {
            width: 0.5rem;
        }

        &::-webkit-scrollbar-track {
            -webkit-box-shadow: inset 0 0 0.3rem rgba(0,0,0,0.3);
        }

        &::-webkit-scrollbar-thumb {
            background-color: darkgrey;
            outline: none;
        }

        .is-right {
            display: flex;
            justify-content: flex-end;
            margin-right: 0.25rem;
            .is-group-message {
                margin-bottom: -0.3rem;
            }
        }
        .is-left {
            display: flex;
            justify-content: flex-start;
            margin-left: 2rem;
            .is-group-message {
                margin-bottom: -0.3rem;
                .message-user-avatar {
                    display: none;
                }
            }
        }
        .chat-detail-item {
            padding: 0.35rem 0.5rem 0.45rem;
            white-space: pre-wrap;
            box-shadow: 0 12px 28px 0 rgba(0,0,0,0.2),0 2px 4px 0 rgba(0,0,0,0.1);
            margin: 0.5rem 0;
            outline: none;
            max-width: 70%;
            position: relative;
        }
        .chat-detail-list-left {
            background-color: #E4E6EB;
            border-top-right-radius: 2rem;
            border-bottom-right-radius: 2rem;
            .message-user-avatar {
                position: absolute;
                bottom: 0;
                left: -2rem;
            }
            .message-time {
                position: absolute;
                top: calc(50% - 0.65rem);
                right: -10.5rem;
                color: #000;
                width: 9.5rem;
            }
            &:hover {
                .message-time {
                    visibility: visible;
                }
            }
            &:not(:hover) {
                .message-time {
                    visibility: hidden;
                }
            }
        }
        .chat-detail-list-right {
            background-color: rgb(19, 207, 19);
            border-top-left-radius: 2rem;
            border-bottom-left-radius: 2rem;
            color: white;
            .message-time {
                position: absolute;
                text-align: right;
                top: calc(50% - 0.65rem);
                left: -10.5rem;
                color: #000;
                width: 9.5rem;
            }
            &:hover {
                .message-time {
                    visibility: visible;
                }
            }
            &:not(:hover) {
                .message-time {
                    visibility: hidden;
                }
            }
        }
    }
    .chat-detail-chat-box {
        position: relative;
        #input-message {
            height: 4rem;
            padding-right: 3rem;
        }
        .create-message-button {
            position: absolute;
            right: 0.75rem;
            top: 1rem;
            padding: 0.1rem 0.25rem;
            background-color: transparent;
            border-color: transparent;
            &:hover {
                background-color: transparent;
                border-color: transparent;
                opacity: 0.7;
            }
        }
    }
}
</style>