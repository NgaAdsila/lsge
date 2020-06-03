import { API_PATH } from './constants';
import ApiService from '../helper/ApiService';

export async function filterByUser(req = {}) {
    return await ApiService.post(API_PATH.LOGIN_HISTORY_FILTER_BY_USER, req, {});
}