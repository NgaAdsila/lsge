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
            <hr />
            <div class="friend-list-wrapper">
                <div v-if="friendList && friendList.length">
                    <div v-for="friend in friendList" :key="friend.id"
                         class="friend-list-item has-link"
                         @click.stop.prevent="createChatRoom(friend)">
                        <div class="friend-item-info" :class="{ 'offline': !onlineFriend(friend.id) }">
                            <div class="friend-item-avatar">
                                <b-avatar class="text-uppercase"
                                          :style="{ 'background-color': getColor() + ' !important' }"
                                          :text="friend.name ? friend.name.charAt(0) : ''"></b-avatar>
                            </div>
                            <div class="friend-item-name">
                                {{ friend.name }}
                            </div>
                        </div>
                        <div class="friend-item-status">
                            <b-icon v-show="onlineFriend(friend.id)" icon="dot" variant="success" scale="3"></b-icon>
                        </div>
                    </div>
                </div>
                <div v-else>
                    <b-alert variant="warning" show>{{ $t('common.message.no_data') }}</b-alert>
                </div>
            </div>
        </b-sidebar>
    </div>
</template>

<script>
    import { randomColor } from "../../utils";

    export default {
        name: "FriendList",
        props: [
            'friendList',
            'onlineUserIds'
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
            createChatRoom(user) {
                this.$emit('createChatRoom', user);
            },
            onlineFriend(userId) {
                return this.onlineUserIds && this.onlineUserIds.includes(userId);
            },
            getColor() {
                return randomColor()
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
            margin: 1rem;
            .friend-list-item {
                display: flex;
                justify-content: space-between;
                padding: 0.5rem 0;
                .friend-item-info {
                    display: flex;
                    line-height: 3rem;
                    width: 100%;
                    .friend-item-avatar {
                        margin-right: 0.5rem;
                    }
                    .friend-item-name {
                        white-space: nowrap;
                        text-overflow: ellipsis;
                        overflow: hidden;
                        width: calc(100% - 4rem);
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
                .friend-item-status {
                    line-height: 3rem;
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
    }
}
</style>