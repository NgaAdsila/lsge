import { API_PATH } from './constants';
import ApiService from '../helper/ApiService';

export async function addFriend(req = {}) {
    return await ApiService.post(API_PATH.RELATIONSHIP_ADD_FRIEND, req, {});
}

export async function cancelFriend(req = {}) {
    return await ApiService.put(API_PATH.RELATIONSHIP_CANCEL_FRIEND, req, {});
}

export async function approveFriend(req = {}) {
    return await ApiService.put(API_PATH.RELATIONSHIP_APPROVE_FRIEND, req, {});
}