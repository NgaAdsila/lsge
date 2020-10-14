import ApiService from '../helper/ApiService';
import {API_PATH, RESPONSE, success, fail, ECHO_API_URL} from './constants';
import store from '../store/index';
import { getBrowser, getOs } from "@/helper/detect_browser";
import StorageHelper from "@/helper/StorageHelper";

async function loginEchoServer(user) {
    try {
        const echoRes = await ApiService.post(API_PATH.ECHO_AUTH_LOGIN, {}, {}, ECHO_API_URL, user.jwt);
        if (echoRes.status === RESPONSE.STATUS.SUCCESS) {
            user.echoJwt = echoRes.data.token;
        }
    } catch (e) {
        console.log('Login echo server error: ', e);
    }
}

export async function login(data = {}) {
    try {
        const browser = getBrowser();
        const os = getOs();
        data.browser = browser.name + ' v.' + browser.version + ' (' + os.name + ')';
        const res = await ApiService.post(API_PATH.AUTH_LOGIN, data, {});
        if (res.status === RESPONSE.STATUS.ERROR) {
            return res;
        }

        if (res.data && res.data.user) {
            const user = {
                id: res.data.user.id,
                name: res.data.user.name,
                role: res.data.user.authorities[0].authority,
                jwt: res.data.accessToken,
                isLogin: true,
                color: res.data.user.color,
                avatar: res.data.user.avatar
            };
            await loginEchoServer(user);
            store.commit('doLogin', user);
            StorageHelper.setValue(StorageHelper.STORAGE_KEY, user);

            return success(user);
        }
        return fail('exception', RESPONSE.CODE.EXCEPTION);
    } catch (e) {
        return fail(e.message, e.status);
    }
}

export async function register(data = {}) {
    return await ApiService.post(API_PATH.AUTH_SIGNUP, data, {});
}

export async function signOut() {
    await store.commit('reset');
    await StorageHelper.removeByKey(StorageHelper.STORAGE_KEY);
}

export async function getCurrentUser() {
    return await ApiService.get(API_PATH.CURRENT_USER, {});
}

export async function update(req = {}) {
    let data = new FormData();
    data.append("id", req.id);
    data.append("name", req.name);
    data.append("email", req.email);
    data.append("color", req.color ? req.color : '');
    data.append("avatar", req.avatarFile);
    const res = await ApiService.put(API_PATH.USER_UPDATE, data, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
    if (res.status === RESPONSE.STATUS.SUCCESS &&
        (store.getters.name !== req.name || store.getters.color !== req.color || store.getters.avatar !== res.data.avatar)) {
        store.commit('saveName', {
            name: req.name
        });
        store.commit('saveColor', {
            color: req.color
        });
        store.commit('saveAvatar', {
            avatar: res.data.avatar
        });
        const userLocal = StorageHelper.getValue(StorageHelper.STORAGE_KEY);
        userLocal.name = req.name;
        userLocal.color = req.color;
        userLocal.avatar = res.data.avatar;
        StorageHelper.setValue(StorageHelper.STORAGE_KEY, userLocal);
    }
    return res;
}

export async function changePassword(req = {}) {
    return await ApiService.put(API_PATH.USER_CHANGE_PASSWORD, req, {});
}

export async function refreshToken() {
    try {
        const res = await ApiService.post(API_PATH.AUTH_REFRESH_TOKEN, { id: store.getters.id }, {});
        if (res.status === RESPONSE.STATUS.SUCCESS) {
            store.commit('saveJwt', res.data.message);
            const userLocal = StorageHelper.getValue(StorageHelper.STORAGE_KEY);
            userLocal.jwt = res.data.message;
            StorageHelper.setValue(StorageHelper.STORAGE_KEY, userLocal);
            return success('Refresh Token success!');
        }
        return res;
    } catch (e) {
        return fail(e.message);
    }
}

export async function forgetPassword(req = {}) {
    return await ApiService.post(API_PATH.AUTH_FORGET_PASSWORD, req, {});
}

export async function getList(keyword) {
    return await ApiService.get(API_PATH.USER_GET_LIST + (keyword ? `?keyword=${keyword}` : ''), {});
}

export async function filter(req = {}) {
    return await ApiService.post(API_PATH.USER_FILTER, req, {});
}

export async function getRoleOptions() {
    return await ApiService.get(API_PATH.USER_ROLE_OPTION, {});
}

export async function initResetPassword(req = {}) {
    return await ApiService.post(API_PATH.AUTH_INIT_RESET_PASSWORD, req, {});
}

export async function resetPassword(req = {}) {
    return await ApiService.put(API_PATH.AUTH_RESET_PASSWORD, req, {});
}

export async function bandUsers(req = {}) {
    return await ApiService.put(API_PATH.USER_BAND, req, {}, ECHO_API_URL, store.getters.echoJwt);
}

export async function activeUsers(req = {}) {
    return await ApiService.put(API_PATH.USER_ACTIVE, req, {});
}

export async function resetPasswordUsers(req = {}) {
    return await ApiService.put(API_PATH.USER_RESET_PASSWORD, req, {}, ECHO_API_URL, store.getters.echoJwt);
}

export async function updateRoleUsers(req = {}) {
    return await ApiService.put(API_PATH.USER_UPDATE_ROLE, req, {}, ECHO_API_URL, store.getters.echoJwt);
}

export async function getRole() {
    return await ApiService.get(API_PATH.USER_ROLE, {});
}