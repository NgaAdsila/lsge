<template>
    <div class="find-friend-component">
        <div v-if="userList.length > 0" class="find-friend-list">
            <div v-for="(user, index) in userList" :key="index" class="find-friend-item">
                <div class="user-info">
                    <div class="user-avatar">
                        <b-avatar class="text-uppercase"
                                  :text="user.name ? user.name.charAt(0) : ''"></b-avatar>
                    </div>
                    <div class="user-name">
                        {{ user.name }}
                    </div>
                </div>
                <div class="user-action">
                    <div v-if="user.hasFriendReq" class="user-action-is-friend">
                        <b-icon v-if="user.canApprove" icon="person-check"
                                variant="success" font-scale="1.5"
                                class="has-link user-action-approve"
                                @click="approveFriend(user.id, index)"></b-icon>
                        <b-icon icon="person-x"
                                variant="danger" font-scale="1.5"
                                class="has-link"
                                @click="cancelFriend(user.id, index)"></b-icon>
                    </div>
                    <b-icon v-else icon="person-plus"
                            variant="primary" font-scale="1.5"
                            class="has-link"
                            @click="addFriend(user.id, index)"></b-icon>
                </div>
                <div v-show="isFriend(user)" class="user-flag-friendly">
                    <b-icon icon="star-fill" font-scale="0.75"></b-icon>
                </div>
            </div>
        </div>
        <div v-else class="find-friend-no-data">
            <b-alert variant="warning" show>{{ $t('common.message.no_data') }}</b-alert>
        </div>
    </div>
</template>

<script>
    import { RELATION_STATUS } from '../../services/constants';
    export default {
        name: "FindFriendComponent",
        props: [
            'userList'
        ],
        methods: {
            addFriend(userId, index) {
                this.$emit('addFriend', userId, index);
            },
            cancelFriend(userId, index) {
                this.$emit('cancelFriend', userId, index);
            },
            approveFriend(userId, index) {
                this.$emit('approveFriend', userId, index);
            },
            isFriend(user) {
                return user.hasFriendReq && user.status === RELATION_STATUS.APPROVED;
            }
        }
    }
</script>

<style lang="scss" scoped>
.find-friend-component {
    max-width: 700px;
    margin: 0 auto;
    .find-friend-list {
        .find-friend-item {
            display: flex;
            flex-direction: row;
            justify-content: space-between;
            margin-bottom: 1rem;
            padding: 0.5rem 1rem;
            border: 1px solid rgba(0,0,0, 0.1);
            border-radius: 0.5rem;
            box-shadow: 2px 2px rgba(0,0,0, 0.1);
            height: 5rem;
            position: relative;
            .user-info {
                display: flex;
                flex-direction: row;
                .user-avatar {
                    line-height: 3.5rem;
                }
                .user-name {
                    margin-left: 0.5rem;
                    line-height: 3.75rem;

                }
            }
            .user-action {
                line-height: 4rem;
                .user-action-is-friend {
                    .user-action-approve {
                        margin-right: 0.5rem;
                    }
                }
            }
            .user-flag-friendly {
                position: absolute;
                top: 0;
                right: 0.275rem;
                padding: 0 0.075rem;
                background-color: #28a745;

                .b-icon.bi {
                    color: white;
                }
            }
        }
    }
}
</style>