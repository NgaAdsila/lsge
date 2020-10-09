const USER_ROLES = {
    USER: {
        ROLE: 'ROLE_USER',
        ROUTE_NAMES: [
            'Home',
            'LoginHistory',
            'Profile',
            'AboutMe',
            'ContactUs',
            'FindFriend',
            'ChatDetail'
        ]
    },
    ADMIN: {
        ROLE: 'ROLE_ADMIN',
        ROUTE_NAMES: [
            'Home',
            'LoginHistory',
            'Profile',
            'AboutMe',
            'ContactUs',
            'FindFriend',
            'ChatDetail',
            'ManagerHome'
        ]
    }
}

export function checkRole(routeName, res) {
    if (!res || !('responses' in res) || res.responses.length === 0) {
        return false;
    }
    const roles = res.responses;
    return (roles.includes(USER_ROLES.ADMIN.ROLE) && USER_ROLES.ADMIN.ROUTE_NAMES.includes(routeName))
        || (roles.includes(USER_ROLES.USER.ROLE) && USER_ROLES.USER.ROUTE_NAMES.includes(routeName));
}