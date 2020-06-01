import ApiService from '../helper/ApiService';
import {API_PATH, success, fail} from './constants';
import store from '../store/index';

export async function login(data = {}) {
    try {
        const res = await ApiService.post(API_PATH.AUTH_LOGIN, data, {});

        store.commit('doLogin', {
            userId: res.data.id,
            name: res.data.name,
            role: res.data.authorities[0].authority
        });
        store.commit('saveJwt', res.data.tokenType + ' ' + res.data.accessToken);

        return success({
            enabled: res.data.enabled,
            id: res.data.id,
            name: res.data.name,
            username: res.data.username,
            role: res.data.authorities[0].authority
        });
    } catch (e) {
        return fail(e.message, e.status);
    }
}

export async function register(data = {}) {
    return await ApiService.post(API_PATH.AUTH_SIGNUP, data, {});
}