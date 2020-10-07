<template>
    <div class="friend-list-page">
        <b-button v-b-toggle.sidebar-list-friend
                  ref="friendListButton"
                  class="friend-list-button">
            <b-icon icon="people"></b-icon>
            <div class="friend-list-status">
                <div v-show="countOnlineFriend > 0"
                     v-b-tooltip.hover.topleft="countOnlineFriend > 1
                        ? $t('friend-list.label.online-numbers', { number: countOnlineFriend })
                        : $t('friend-list.label.online-number')"
                     class="friend-list-count">
                    <b-icon icon="dot"
                            animation="throb"
                            scale="2"></b-icon><span>{{ countOnlineFriend }}</span>
                </div>
                <div v-show="countNewMessage > 0"
                     v-b-tooltip.hover.topleft="countNewMessage > 1
                        ? $t('friend-list.label.new-message-numbers', { number: countNewMessage })
                        : $t('friend-list.label.new-message-number')"
                     class="friend-new-message-count">
                    <b-icon icon="chat-dots"
                            animation="throb"
                            scale="0.5"></b-icon><span>{{ countNewMessage }}</span>
                </div>
            </div>
        </b-button>
        <b-sidebar id="sidebar-list-friend" :title="$t('common.label.friend-list')"
                   v-model="isToggleSidebarOpen"
                   right shadow>
            <div class="friend-list-wrapper">
                <b-overlay :show="isLoading" rounded="sm" spinner-variant="primary">
                    <b-tabs content-class="mt-3" justified>
                        <b-tab :title="$t('friend-list.label.tab-friends', { number: friendList.length })"
                               active class="friend-list-tab">
                            <div v-if="friendList && friendList.length">
                                <div v-for="friend in orderFriendList(friendList)" :key="friend.id"
                                     class="friend-list-item" :class="{ 'first-offline': friend.isFirst }">
                                    <div class="friend-item-info has-link"
                                         @click.stop.prevent="createChatRoom(friend)">
                                        <div class="friend-item-avatar">
                                            <b-avatar class="text-uppercase"
                                                      :style="{ 'background-color': getColor(friend) }"
                                                      :text="friend.name ? friend.name.charAt(0) : ''"></b-avatar>
                                            <b-icon icon="dot"
                                                    :class="friend.isOnline ? 'is-online' : 'is-offline'"
                                                    scale="4" class="friend-item-status"></b-icon>
                                        </div>
                                        <div class="friend-item-name">
                                            <div class="friend-item-name-text">
                                                {{ friend.name }}
                                            </div>
                                            <div v-show="friend.lastMessage" class="friend-item-last-message"
                                                 :class="{ 'is-read': friend.isReadLastMessage}">
                                                {{ friend.lastMessage ? friend.lastMessage.message : '' }}
                                            </div>
                                        </div>
                                    </div>
                                    <div class="friend-item-actions">
                                        <b-icon icon="person-x"
                                                variant="danger" font-scale="1.5"
                                                class="has-link"
                                                @click="cancelFriend(friend)"></b-icon>
                                    </div>
                                </div>
                            </div>
                            <div v-else>
                                <b-alert variant="warning" show>{{ $t('common.message.no_data') }}</b-alert>
                            </div>
                        </b-tab>
                        <b-tab :title="$t('friend-list.label.tab-requests', { number: requestedFriends.length })"
                               class="friend-list-tab">
                            <div v-if="requestedFriends && requestedFriends.length">
                                <div v-for="user in requestedFriends" :key="user.id" class="friend-list-item">
                                    <div class="friend-item-info">
                                        <div class="friend-item-avatar">
                                            <b-avatar class="text-uppercase"
                                                      :style="{ 'background-color': getColor() + ' !important' }"
                                                      :text="user.name ? user.name.charAt(0) : ''"></b-avatar>
                                            <b-icon icon="dot"
                                                    :class="user.isOnline ? 'is-online' : 'is-offline'"
                                                    scale="4" class="friend-item-status"></b-icon>
                                        </div>
                                        <div class="friend-item-name-request">
                                            {{ user.name }}
                                        </div>
                                    </div>
                                    <div class="friend-item-actions">
                                        <b-icon icon="person-check"
                                                variant="success" font-scale="1.5"
                                                class="has-link user-action-approve"
                                                @click="approveFriend(user)"></b-icon>
                                        <b-icon icon="person-x"
                                                variant="danger" font-scale="1.5"
                                                class="has-link"
                                                @click="cancelFriend(user)"></b-icon>
                                    </div>
                                </div>
                            </div>
                            <div v-else>
                                <b-alert variant="warning" show>{{ $t('common.message.no_data') }}</b-alert>
                            </div>
                        </b-tab>
                    </b-tabs>
                </b-overlay>
            </div>
        </b-sidebar>
        <div v-if="isToggleSidebarOpen" @click="isToggleSidebarOpen = false" class="navbar-backdrop"></div>
    </div>
</template>

<script>
    import { randDarkColor } from "@/utils";

    export default {
        name: "FriendList",
        props: [
            'friendList',
            'requestedFriends',
            'onlineUserIds',
            'isLoading',
            'currentUserId'
        ],
        data() {
            return {
                isToggleSidebarOpen: false
            }
        },
        computed: {
            countOnlineFriend: function() {
                if (this.friendList && this.onlineUserIds && this.onlineUserIds.length > 1) {
                    let count = 0
                    this.friendList.forEach(f => {
                        if (this.onlineUserIds.includes(f.id)) {
                            count++
                        }
                    })
                    return count
                }
                return 0
            },
            countNewMessage: function () {
                if (this.friendList) {
                    let count = 0
                    this.friendList.forEach(f => {
                        if (f.lastMessage && f.lastMessage.statuses.some(s => s.userId === this.currentUserId && !s.seen)) {
                            count++
                        }
                    })
                    return count
                }
                return 0
            }
        },
        methods: {
            getFriendList() {
                this.$emit('getFriendList')
            },
            createChatRoom(user) {
                this.$emit('createChatRoom', user);
            },
            onlineFriend(userId) {
                return this.onlineUserIds && this.onlineUserIds.includes(userId);
            },
            getColor(user) {
                return user.color || randDarkColor()
            },
            approveFriend(user) {
                this.$emit('approveFriend', user);
            },
            cancelFriend(user) {
                this.$emit('cancelFriend', user);
            },
            orderFriendList(friendList) {
                if (friendList.length <= 0) {
                    return []
                }
                let onlineList = [],
                    offlineList = []
                friendList.forEach(f => {
                    f.isReadLastMessage = f.lastMessage
                        && f.lastMessage.statuses.some(s => s.userId === this.currentUserId && s.seen)
                    if (this.onlineFriend(f.id)) {
                        f.isOnline = true
                        f.isFirst = false
                        onlineList.push(f)
                    } else {
                        f.isOnline = false
                        f.isFirst = false
                        offlineList.push(f)
                    }
                })
                if (onlineList.length && offlineList.length) {
                    offlineList[0].isFirst = true
                }
                return onlineList.concat(offlineList)
            }
        }
    }
</script>

<style lang="scss" scoped>
    .friend-list-page {
        .navbar-backdrop {
            z-index: 1031;
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
        }
        .friend-list-button {
            position: fixed;
            bottom: 5%;
            right: 2%;
            z-index: 1000;
            .friend-list-status {
                position: absolute;
                left: 2.15rem;
                top: -0.65rem;
                font-weight: 600;
                font-size: 70%;
                min-width: 1.5rem;
                white-space: nowrap;
                text-align: left;
                .b-icon.bi {
                  font-size: 100%;
                  color: #FFFFFF;
                }
                .friend-list-count {
                    background: #28a745;
                    border-radius: 0.25rem;
                    margin-bottom: 0.05rem;
                    padding-right: 0.15rem;
                }
                .friend-new-message-count {
                    background: #007bff;
                    border-radius: 0.25rem;
                    margin-bottom: 0.05rem;
                    padding-right: 0.15rem;
                }
            }
        }
        .friend-list-wrapper {
            .friend-list-tab {
                margin: 0 1rem 1rem;
            }
            .friend-list-item {
                display: flex;
                justify-content: space-between;
                padding: 0.5rem 0;
                &.first-offline {
                    border-top: 1px solid cyan;
                }
                .friend-item-info {
                    display: flex;
                    line-height: 3rem;
                    width: calc(100% - 3.5rem);
                    .friend-item-avatar {
                        margin-right: 0.5rem;
                        position: relative;
                        .friend-item-status {
                            position: absolute;
                            right: -4px;
                            bottom: 2px;
                            border-radius: 100%;
                            &.is-online {
                                background: #FFFFFF;
                                color: #28a745;
                            }
                            &.is-offline {
                              background: #FFFFFF;
                              color: rgba(0,0,0,0.1);
                            }
                        }
                    }
                    .friend-item-name {
                        white-space: nowrap;
                        line-height: 1rem;
                        padding-top: 0.95rem;
                        width: calc(100% - 3rem);
                        .friend-item-name-text {
                            text-overflow: ellipsis;
                            overflow: hidden;
                        }
                        .friend-item-last-message {
                            font-size: 0.65rem;
                            font-style: italic;
                            text-overflow: ellipsis;
                            overflow: hidden;
                            &:not(.is-read) {
                                font-weight: bold;
                            }
                            &.is-read {
                                color: rgba(0,0,0,0.5);
                            }
                        }
                    }
                    .friend-item-name-request {
                        white-space: nowrap;
                        text-overflow: ellipsis;
                        overflow: hidden;
                        width: calc(100% - 3rem);
                    }
                }
                .friend-item-actions {
                    line-height: 3rem;
                    .user-action-approve {
                        margin-right: 0.5rem;
                    }
                }
            }
        }
    }
</style>

<style lang="scss">
.friend-list-page {
    #sidebar-list-friend {
        top: 4.75rem;
        height: calc(100vh - 4.75rem);

        .b-sidebar-body {
            .tab-content {
                height: calc(100vh - 12rem);
                overflow: auto;
                &::-webkit-scrollbar {
                    width: 0;
                }
                overscroll-behavior: contain;
            }
        }
    }
}
</style>