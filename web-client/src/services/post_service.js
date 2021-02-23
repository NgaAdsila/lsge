import ApiService from "../helper/ApiService";
import {API_PATH, ECHO_API_URL} from "./constants";
import store from '../store/index';

export async function getList(req = {}) {
    return await ApiService.post(API_PATH.POST_GET_LIST, req, {})
}

export async function createComment(id, req = {}) {
    return await ApiService.post(`${API_PATH.POST_API}/${id}/create-comment`, req, {}, ECHO_API_URL, store.getters.echoJwt)
}

export async function getById(id) {
    return await ApiService.get(`${API_PATH.POST_API}/${id}`, {});
}

export async function like(id) {
    return await ApiService.post(`${API_PATH.POST_API}/${id}/like`, {}, {}, ECHO_API_URL, store.getters.echoJwt)
}

export async function dislike(id) {
    return await ApiService.post(`${API_PATH.POST_API}/${id}/dislike`, {}, {}, ECHO_API_URL, store.getters.echoJwt)
}

export async function createPost(title, content, shareMode) {
    return await ApiService.post(API_PATH.POST_API, {
        title: title,
        content: content,
        shareMode: shareMode
    }, {}, ECHO_API_URL, store.getters.echoJwt)
}