const app = {
    state: {
        friendRequestedIds: [],
        friendApprovedIds: [],
        friendCancelledIds: []
    },
    mutations: {
        addFriendRequestedId: (state, userId) => {
            state.friendRequestedIds.push(userId)
        },
        addFriendApprovedId: (state, userId) => {
            state.friendApprovedIds.push(userId)
        },
        addFriendCancelledId: (state, userId) => {
            state.friendCancelledIds.push(userId)
        },
        removeFriendRequestedId: (state, userId) => {
            const index = state.friendRequestedIds.findIndex(id => id === userId)
            if (index > -1) {
                state.friendRequestedIds.splice(index, 1)
            }
        },
        removeFriendApprovedId: (state, userId) => {
            const index = state.friendApprovedIds.findIndex(id => id === userId)
            if (index > -1) {
                state.friendApprovedIds.splice(index, 1)
            }
        },
        removeFriendCancelledId: (state, userId) => {
            const index = state.friendCancelledIds.findIndex(id => id === userId)
            if (index > -1) {
                state.friendCancelledIds.splice(index, 1)
            }
        }
    }
}

export default app