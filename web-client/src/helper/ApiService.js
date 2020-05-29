import axios from 'axios'
import { RESPONSE, success, fail } from '../services/constants'
import store from '../store/index'

const URL = process.env.BACKEND_BASE_URL || 'http://localhost:18081';
const instance = axios.create({
    baseURL: URL,
    timeout: process.env.API_TIMEOUT || 10000,
    headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*'
    }
});

export default {
    getAxios: () => {
        instance.defaults.headers.common['Authorization'] = 'Bearer ' + store.getters.jwt;
        return instance;
    },
    get: async (url, config = {}) => {
        try {
            const res = await this.getAxios().get(url, config);
            return success(res.data);
        } catch (e) {
            return this.handleError(e);
        }
    },
    post: async (url, data= {}, config = {}) => {
        try {
            const res = await this.getAxios().post(url, data, config);
            return success(res.data);
        } catch (e) {
            return this.handleError(e);
        }
    },
    put: async (url, data = {}, config = {}) => {
        try {
            const res = await this.getAxios().put(url, data, config);
            return success(res.data);
        } catch (e) {
            return this.handleError(e);
        }
    },
    remove: async (url, config = {}) => {
        try {
            const res = await this.getAxios().delete(url, config);
            return success(res.data);
        } catch (e) {
            return this.handleError(e);
        }
    },
    handleError: async (e) => {
        if (!e || !e.status || !e.message) {
            return fail('Exception', RESPONSE.CODE.EXCEPTION);
        }
        return fail(e.message, e.status);
    }
}