<template>
    <div class="chat-detail-component">
        <div class="chat-detail-header">

        </div>
        <div class="chat-detail-list">
            <div v-if="messages.length > 0">
                <div v-for="message in messages" :key="message.id">
                    <div :class="message.createdBy === currentUserId ? 'chat-detail-list-right' : 'chat-detail-list-left'">
                        <div class="message-header">
                           <span class="message-user-name">{{ displayUserName(message.createdBy) }}</span>
                            <span class="message-time">{{ displayTime(message.createdAt) }}</span>
                        </div>
                        <div class="message-content">
                            {{ message.message }}
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="chat-detail-chat-box">

        </div>
    </div>
</template>

<script>
    export default {
        name: "ChatDetailComponent",
        props: [
            'chatroomName',
            'currentUserId',
            'messages',
            'users'
        ],
        methods: {
            displayUserName(userId) {
                if (this.users.length <= 0 || !this.users[userId]) {
                    return this.$t('common.label.someone');
                }
                return this.users[userId].nickname || this.users[userId].name;
            },
            displayTime(time) {
                let d = new Date(time),
                    month = '' + (d.getMonth() + 1),
                    day = '' + d.getDate(),
                    year = d.getFullYear(),
                    hour = d.getHours(),
                    minute = d.getMinutes(),
                    second = d.getSeconds();

                if (month.length < 2)
                    month = '0' + month;
                if (day.length < 2)
                    day = '0' + day;
                if (hour.length < 2)
                    hour = '0' + hour;
                if (minute.length < 2)
                    minute = '0' + minute;
                if (second.length < 2)
                    second = '0' + second;

                return [day, month, year].join('/') + ' ' + [hour, minute, second].join(':');
            }
        }
    }
</script>

<style lang="scss" scoped>

</style>