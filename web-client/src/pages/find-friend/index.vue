<template>
    <div class="find-friend-page">
        <b-breadcrumb :items="crumbItems"></b-breadcrumb>
        <b-overlay :show="isLoading" rounded="sm" spinner-variant="primary">
            <FindFriendComponent :userList="userList"
                                 @addFriend="addFriend"
                                 @cancelFriend="cancelFriend" />
        </b-overlay>
    </div>
</template>

<script>
    import FindFriendComponent from "../../components/find-friend/FindFriendComponent";
    import { addFriend } from "../../services/relationship_service";
    import {RESPONSE} from "../../services/constants";

    export default {
        name: "FindFriend",
        components: {FindFriendComponent},
        data() {
            return {
                crumbItems: [
                    {
                        text: this.$t('common.label.home'),
                        href: '/home'
                    },
                    {
                        text: this.$t('common.label.find-friend'),
                        active: true
                    }
                ],
                isLoading: false,
                userList: [
                    {
                        id: 1,
                        name: 'User 1',
                        isFriend: false
                    },
                    {
                        id: 2,
                        name: 'User 2',
                        isFriend: false
                    },
                    {
                        id: 3,
                        name: 'User 3',
                        isFriend: true
                    },
                    {
                        id: 4,
                        name: 'User 4',
                        isFriend: false
                    },
                    {
                        id: 5,
                        name: 'User 5',
                        isFriend: false
                    },
                    {
                        id: 6,
                        name: 'User 6',
                        isFriend: false
                    }
                ]
            }
        },
        computed: {
            keyword: function () {
                return this.$route.query.keyword;
            }
        },
        methods: {
            async addFriend(userId, index) {
                try {
                    this.isLoading = true;
                    const res = await addFriend({recUserId: userId});
                    if (res.status === RESPONSE.STATUS.SUCCESS) {
                        this.userList[index].isFriend = true;
                        this.$bvToast.toast(this.$t('common.message.update_success'), {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'success',
                            autoHideDelay: 2000
                        });
                    } else {
                        this.$bvToast.toast(res.message, {
                            title: this.$t('common.toast.title'),
                            toaster: 'b-toaster-top-center',
                            solid: true,
                            variant: 'danger',
                            autoHideDelay: 2000
                        });
                    }
                } catch(e) {
                    console.log('add friend error: ', e);
                } finally {
                    this.isLoading = false;
                }
            },
            cancelFriend(userId, index) {
                console.log('CANCEL FRIEND: ', userId, index);
            }
        }
    }
</script>

<style scoped>

</style>