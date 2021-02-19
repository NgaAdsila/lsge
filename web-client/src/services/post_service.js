import ApiService from "../helper/ApiService";
import {API_PATH, ECHO_API_URL} from "./constants";
import store from '../store/index';

export async function getList(req = {}) {
    return await ApiService.post(API_PATH.POST_GET_LIST, req, {})
}

export async function createComment(id, req = {}) {
    return await ApiService.post(`/api/posts/${id}/create-comment`, req, {}, ECHO_API_URL, store.getters.echoJwt)
}