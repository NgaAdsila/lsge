import { API_PATH } from './constants';
import ApiService from '../helper/ApiService';

export async function initNormalChatroom(req = {}) {
    return await ApiService.post(API_PATH.CHATROOM_INIT_NORMAL, req, {});
}

export async function findById(id) {
    return await ApiService.get(`${API_PATH.CHATROOM_API}/${id}`, {});
}