import StorageHelper from "../helper/StorageHelper";
import store from '../store/index'

export const USER_ROLES = {
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
            'ManagerHome',
            'ManagerUser',
            'ManagerFile'
        ]
    }
}

export function checkRole(routeName, res) {
    if (!res || !('responses' in res) || res.responses.length === 0) {
        return false;
    }
    const roles = res.responses;
    const userInfo = StorageHelper.getValue(StorageHelper.STORAGE_KEY);
    if (roles.includes(USER_ROLES.ADMIN.ROLE)) {
        store.commit('setUserRole', USER_ROLES.ADMIN.ROLE);
        userInfo.role = USER_ROLES.ADMIN.ROLE;
    } else {
        store.commit('setUserRole', USER_ROLES.USER.ROLE);
        userInfo.role = USER_ROLES.USER.ROLE;
    }
    StorageHelper.setValue(StorageHelper.STORAGE_KEY, userInfo);
    return (roles.includes(USER_ROLES.ADMIN.ROLE) && USER_ROLES.ADMIN.ROUTE_NAMES.includes(routeName))
        || (roles.includes(USER_ROLES.USER.ROLE) && USER_ROLES.USER.ROUTE_NAMES.includes(routeName));
}