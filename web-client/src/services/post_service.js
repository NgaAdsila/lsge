import ApiService from "../helper/ApiService";
import {API_PATH} from "./constants";

export async function getList(req = {}) {
    return await ApiService.post(API_PATH.POST_GET_LIST, req, {});
}