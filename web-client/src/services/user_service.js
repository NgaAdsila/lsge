import ApiService from '../helper/ApiService';
import { API_PATH } from './constants';

export async function login(data = {}) {
    return await ApiService.login(API_PATH.AUTH_LOGIN, data, {});
}

export async function register(data = {}) {
    return await ApiService.post(API_PATH.AUTH_SIGNUP, data, {});
}