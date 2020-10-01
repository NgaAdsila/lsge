const getters = {
    isLogin: state => state.isLogin,
    jwt: state => state.jwt,
    role: state => state.role,
    id: state => state.id,
    name: state => state.name,
    echoJwt: state => state.echoJwt,

    friendRequestedIds: state => state.app.friendRequestedIds,
    friendApprovedIds: state => state.app.friendApprovedIds,
    friendCancelledIds: state => state.app.friendCancelledIds
}

export default getters