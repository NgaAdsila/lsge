<template>
    <div class="friend-list-page">
        <b-button v-b-toggle.sidebar-list-friend
                  ref="friendListButton"
                  class="friend-list-button">
            <b-icon icon="people"></b-icon>
            <b-avatar v-show="countOnlineFriend > 0" class="friend-list-count"
                      variant="success"
                      :text="'' + countOnlineFriend"></b-avatar>
        </b-button>
        <b-sidebar id="sidebar-list-friend" :title="$t('common.label.friend-list')" right shadow>
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
                                                      :style="{ 'background-color': getColor() + ' !important' }"
                                                      :text="friend.name ? friend.name.charAt(0) : ''"></b-avatar>
                                            <b-icon v-show="friend.isOnline" icon="dot"
                                                    variant="success"
                                                    scale="4" class="friend-item-status"></b-icon>
                                        </div>
                                        <div class="friend-item-name">
                                            <div class="friend-item-name-text">
                                                {{ friend.name }}
                                            </div>
                                            <div v-show="friend.lastMessage" class="friend-item-last-message"
                                                 :class="{ 'is-read': isReadLastMessage(friend.lastMessage)}">
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
                                            <b-icon v-show="user.isOnline" icon="dot"
                                                    variant="success"
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
            getColor() {
                return randDarkColor()
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
                    if (this.onlineFriend(f.id)) {
                        f.isOnline = true
                        f.isFirst = false
                        onlineList.push(f)
                    } else {
                        f.isOnline = false
                        f.isFirst = offlineList.length === 0
                        offlineList.push(f)
                    }
                })
                return onlineList.concat(offlineList)
            },
            isReadLastMessage(lastMessage) {
                return lastMessage && lastMessage.statuses.some(s => s.userId === this.currentUserId && s.seen)
            }
        }
    }
</script>

<style lang="scss" scoped>
    .friend-list-page {
        .friend-list-button {
            position: fixed;
            bottom: 5%;
            right: 2%;
            z-index: 1000;
            .friend-list-count {
                position: absolute;
                top: -0.75rem;
                width: 1.5rem;
                height: 1.5rem;
                font-size: 70%;
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
                            background: #FFFFFF;
                            border-radius: 100%;
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
                    &.offline {
                        .friend-item-avatar {
                            opacity: 0.5;
                        }
                        .friend-item-name {
                            color: darkgrey;
                        }
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