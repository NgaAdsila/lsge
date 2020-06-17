import ApiService from '../helper/ApiService';
import {API_PATH, RESPONSE, success, fail} from './constants';
import store from '../store/index';
import { getBrowser, getOs } from "../helper/detect_browser";

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
                isLogin: true
            };
            store.commit('doLogin', user);
            localStorage.setItem('store', JSON.stringify(user));

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
    await localStorage.removeItem('store');
}

export async function getCurrentUser() {
    return await ApiService.get(API_PATH.CURRENT_USER, {});
}

export async function update(req = {}) {
    const res = await ApiService.put(API_PATH.USER_UPDATE, {
        id: req.id,
        name: req.name,
        email: req.email
    }, {});
    if (res.status === RESPONSE.STATUS.SUCCESS && store.getters.name !== req.name) {
        store.commit('saveName', {
            name: req.name
        });
        const userLocal = JSON.parse(localStorage.getItem('store'));
        userLocal.name = req.name;
        localStorage.setItem('store', JSON.stringify(userLocal));
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
            const userLocal = JSON.parse(localStorage.getItem('store'));
            userLocal.jwt = res.data.message;
            localStorage.setItem('store', JSON.stringify(userLocal));
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