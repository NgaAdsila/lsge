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
                    <b-icon v-if="user.isFriend" icon="person-x"
                            variant="danger" font-scale="1.5"
                            class="has-link"
                            @click="cancelFriend(user.id, index)"></b-icon>
                    <b-icon v-else icon="person-plus"
                            variant="primary" font-scale="1.5"
                            class="has-link"
                            @click="addFriend(user.id, index)"></b-icon>
                </div>
            </div>
        </div>
        <div v-else class="find-friend-no-data">
            <b-alert variant="warning" show>{{ $t('common.message.no_data') }}</b-alert>
        </div>
    </div>
</template>

<script>
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
            }
        }
    }
}
</style>