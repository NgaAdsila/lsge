import ApiService from '../helper/ApiService';
import {API_PATH, RESPONSE, success, fail} from './constants';
import store from '../store/index';

export async function login(data = {}) {
    try {
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