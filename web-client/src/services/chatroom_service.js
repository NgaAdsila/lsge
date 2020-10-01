import {API_PATH, ECHO_API_URL} from './constants';
import ApiService from '../helper/ApiService';
import store from '../store/index';

export async function initNormalChatroom(req = {}) {
    return await ApiService.post(API_PATH.CHATROOM_INIT_NORMAL, req, {});
}

export async function findById(id) {
    return await ApiService.get(`${API_PATH.CHATROOM_API}/${id}`, {});
}

export async function createMessage(req = {}) {
    return await ApiService.post(API_PATH.ECHO_CREATE_MESSAGE, req, {}, ECHO_API_URL, store.getters.echoJwt);
}

export async function isReadMessage(id, req = {}) {
    return await ApiService.put(
        `${API_PATH.CHATROOM_IS_READ_MESSAGE}/${id}`, req, {}, ECHO_API_URL, store.getters.echoJwt);
}