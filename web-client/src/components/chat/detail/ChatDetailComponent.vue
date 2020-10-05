<template>
    <div class="chat-detail-component">
        <div class="chat-detail-header">
            <div class="chat-detail-title">
                {{ chatroomName || getDefaultName() }}
            </div>
            <div class="chat-detail-action">
                <b-dropdown size="md"  variant="link" toggle-class="chat-detail-action-toggle" right no-caret>
                    <template v-slot:button-content>
                        <b-icon icon="gear"></b-icon><span class="sr-only">Search</span>
                    </template>
                    <b-dropdown-item @click="openUpdateChatroomModal">
                        <b-icon icon="plus" scale="0.8" variant="success"
                                animation="fade"></b-icon> {{ $t('chatroom.label.update_chatroom') }}
                    </b-dropdown-item>
                    <b-dropdown-item @click="openSetNicknameModal">
                        <b-icon icon="plus" scale="0.8" variant="success"
                                animation="fade"></b-icon> {{ $t('chatroom.label.set_nickname') }}
                    </b-dropdown-item>
                </b-dropdown>
                <ChatDetailUpdateChatroom :currentName="chatroomName" @onOk="updateChatroom" />
                <ChatDetailSetNickname :currentUsers="users" @onOk="setNickname" />
            </div>
        </div>
        <div class="chat-detail-list" ref="chatDetailList">
            <div v-if="messages.length > 0">
                <div v-for="(message, index) in messages" :key="message.id">
                    <div v-if="isNextDay(message, index)" class="is-default">
                        {{ displayDate(message.createdAt) }}
                        <div class="is-default-line">&#8604;<small>&star;</small>&#128312;<small>&star;</small>&#8605;</div>
                    </div>
                    <div :class="isDefaultMessage(message) ? 'is-default-message' : (message.createdBy === currentUserId ? 'is-right' : 'is-left')">
                        <div v-if="isDefaultMessage(message)" class="chat-detail-item chat-detail-list-left">
                            <div class="message-content text-break" v-linkified
                                 v-html="'&star; ' + message.message + ' &star;'" />
                            <span class="message-time">{{ displayTime(message.createdAt) }}</span>
                        </div>
                        <div v-else-if="message.createdBy === currentUserId"
                             class="chat-detail-item chat-detail-list-right"
                             :class="{'is-group-message': isGroupMessage(message, index) }">
                            <div class="message-content text-break" v-linkified v-html="message.message" />
                            <span class="message-time">{{ displayTime(message.createdAt) }}</span>
                        </div>
                        <div v-else class="chat-detail-item chat-detail-list-left"
                             :class="{'is-group-message': isGroupMessage(message, index) }">
                            <span class="message-user-avatar" :title="displayUserName(message.createdBy)">
                                <b-avatar class="text-uppercase"
                                          size="25"
                                          :text="displayUserName(message.createdBy).charAt(0)"></b-avatar>
                            </span>
                            <div class="message-content text-break" v-linkified v-html="message.message" />
                            <span class="message-time">{{ displayTime(message.createdAt) }}</span>
                        </div>
                    </div>
                    <div v-if="isReadMessage(message, index)" class="is-right message-is-seen-avatar">
                        <b-avatar class="text-uppercase"
                                  size="25"
                                  variant="primary"
                                  :text="displayUserName(currentUserId).charAt(0)"></b-avatar>
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
    import { smartTime, smartDisplayUsername } from "@/utils";
    import ChatDetailUpdateChatroom from "./ChatDetailUpdateChatroom";
    import ChatDetailSetNickname from "./ChatDetailSetNickname";
    import {MESSAGE_TYPE} from "@/services/constants";

    export default {
        name: "ChatDetailComponent",
        components: {ChatDetailSetNickname, ChatDetailUpdateChatroom},
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
            this.scrollToBottom()
        },
        methods: {
            displayUserName(userId) {
                if (this.users.length <= 0) {
                    return this.$t('common.label.someone');
                }
                const user = this.users.find(u => u.id === userId);
                if (!user) {
                    return this.$t('common.label.someone');
                }
                return user.nickname || user.name;
            },
            displayTime(time) {
                return smartTime(time);
            },
            displayDate(time) {
                return time ? new Date(time).toDateString() : '';
            },
            async createMessage() {
                await this.$emit('createMessage', this.newMessage);
                this.newMessage = '';
            },
            isGroupMessage(message, index) {
                const nextMessage = this.messages[index + 1];
                return nextMessage && message.createdBy === nextMessage.createdBy
                    && nextMessage.createdAt - message.createdAt < 60000;
            },
            isReadMessage(message, index) {
                const nextMessage = this.messages[index + 1];
                return message.statuses.some(s => s.userId !== this.currentUserId && s.seen)
                    && (!nextMessage || nextMessage.statuses.some(s => s.userId !== this.currentUserId && !s.seen));
            },
            scrollToBottom() {
                this.$refs.chatDetailList.scrollTop = this.$refs.chatDetailList.scrollHeight;
            },
            isNextDay(message, index) {
                if (index === 0) {
                    return true
                }
                const prevMessage = this.messages[index - 1],
                    prevDate = new Date(prevMessage.createdAt),
                    date = new Date(message.createdAt)
                return prevDate.setHours(0,0,0,0) < date.setHours(0,0,0,0)
            },
            getDefaultName() {
                if (!this.users || this.users.length <= 0) {
                    return this.$t('common.label.chat_detail')
                }
                let userNames = []
                this.users.forEach(u => {
                    if (u.id !== this.currentUserId) {
                        userNames.push(u.name)
                    }
                })
                return userNames.length ? smartDisplayUsername(userNames) : this.$t('common.label.chat_detail')
            },
            openUpdateChatroomModal() {
                this.$bvModal.show('chat-detail-update-chatroom-modal');
            },
            updateChatroom(name) {
                this.$emit('updateChatroom', name)
            },
            openSetNicknameModal() {
                this.$bvModal.show('chat-detail-set-nickname-modal');
            },
            setNickname(users) {
                this.$emit('setNickname', users)
            },
            isDefaultMessage(message) {
                return message.type === MESSAGE_TYPE.DEFAULT
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

    .chat-detail-header {
        text-align: center;
        font-weight: 500;
        font-size: 1rem;
        position: relative;
        margin-bottom: 1rem;

        .chat-detail-action {
            position: absolute;
            right: 0;
            top: -0.5rem;
        }
    }

    .chat-detail-list {
        height: calc(100% - 4.75rem);
        overflow: auto;
        overscroll-behavior: contain;

        &::-webkit-scrollbar {
            width: 0.25rem;
        }

        &::-webkit-scrollbar-track {
            -webkit-box-shadow: inset 0 0 0.3rem rgba(0,0,0,0.3);
        }

        &::-webkit-scrollbar-thumb {
            background-color: darkgrey;
            outline: none;
        }

        .is-default {
            text-align: center;
            font-weight: 500;
            background: radial-gradient(rgba(255,125,0,0.2), transparent);
            .is-default-line {
              margin-top: -0.75rem;
            }
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
            .chat-detail-list-left {
                background-color: #E4E6EB;
                border-top-right-radius: 0.75rem;
                border-bottom-right-radius: 0.75rem;
            }
        }
        .is-default-message {
            display: flex;
            justify-content: flex-start;
            .chat-detail-list-left {
                border-radius: 0.75rem;
                font-style: italic;
                background-color: rgba(0,0,0, 0.5);
                color: #FFFFFF;
                font-size: 0.85rem;
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
            border-top-left-radius: 0.75rem;
            border-bottom-left-radius: 0.75rem;
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