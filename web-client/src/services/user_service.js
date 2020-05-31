import ApiService from '../helper/ApiService';
import { API_PATH } from './constants';

export async function login(data = {}) {
    const user = await ApiService.post(API_PATH.AUTH_LOGIN, data, {});
    return user;
}