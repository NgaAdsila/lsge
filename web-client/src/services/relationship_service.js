import {API_PATH, ECHO_API_URL} from './constants';
import ApiService from '../helper/ApiService';
import store from "@/store";

export async function addFriend(req = {}) {
    return await ApiService.post(API_PATH.RELATIONSHIP_ADD_FRIEND, req, {}, ECHO_API_URL, store.getters.echoJwt);
}

export async function cancelFriend(req = {}) {
    return await ApiService.put(API_PATH.RELATIONSHIP_CANCEL_FRIEND, req, {}, ECHO_API_URL, store.getters.echoJwt);
}

export async function approveFriend(req = {}) {
    return await ApiService.put(API_PATH.RELATIONSHIP_APPROVE_FRIEND, req, {}, ECHO_API_URL, store.getters.echoJwt);
}

export async function getFriendList() {
    return await ApiService.get(API_PATH.RELATIONSHIP_LIST_FRIEND, {});
}