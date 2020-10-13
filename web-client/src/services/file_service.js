import { API_PATH } from './constants';
import ApiService from '../helper/ApiService';

export async function getList(req = {}) {
    return await ApiService.post(API_PATH.FILE_GET_LIST, req, {});
}
export async function readFile(filePath = '') {
    return await ApiService.get(`${API_PATH.FILE_READ}?filePath=${filePath}`,{});
}