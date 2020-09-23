<template>
    <div class="chat-list-page">
        <b-breadcrumb :items="crumbItems"></b-breadcrumb>
        <b-overlay :show="isLoading" rounded="sm" spinner-variant="primary">
            <ChatListComponent />
        </b-overlay>
    </div>
</template>

<script>
    import ChatListComponent from "../../components/chat/ChatListComponent";
    export default {
        name: "Chat",
        components: {ChatListComponent},
        data() {
            return {
                crumbItems: [
                    {
                        text: this.$t('common.label.home'),
                        href: '/home'
                    },
                    {
                        text: this.$t('common.label.chat_list'),
                        active: true
                    }
                ],
                isLoading: true
            }
        }
    }
</script>

<style scoped>

</style>