import axios from 'axios'
import { RESPONSE, success, fail } from '../services/constants'
import store from '../store/index'

const URL = process.env.BACKEND_BASE_URL || 'http://localhost:18081';
const instance = axios.create({
    baseURL: URL,
    timeout: process.env.API_TIMEOUT || 10000,
    headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Expose-Headers': 'Access-Token, Uid'
    }
});

export default {
    getAxios() {
        instance.defaults.headers.common['Authorization'] = 'Bearer ' + store.getters.jwt;
        return instance;
    },
    async get(url, config = {}) {
        try {
            const res = await this.getAxios().get(url, config);
            return success(res.data);
        } catch (e) {
            return this.handleError(e);
        }
    },
    async post(url, data= {}, config = {}) {
        try {
            const res = await this.getAxios().post(url, data, config);
            return success(res.data);
        } catch (e) {
            return this.handleError(e);
        }
    },
    async put(url, data = {}, config = {}) {
        try {
            const res = await this.getAxios().put(url, data, config);
            return success(res.data);
        } catch (e) {
            return this.handleError(e);
        }
    },
    async remove(url, config = {}) {
        try {
            const res = await this.getAxios().delete(url, config);
            return success(res.data);
        } catch (e) {
            return this.handleError(e);
        }
    },
    async handleError(e) {
        if (!e || !e.status || !e.message) {
            return fail('Exception', RESPONSE.CODE.EXCEPTION);
        }
        return fail(e.message, e.status);
    },
    async login(url, data = {}, config = {}) {
        try {
            const res = await this.getAxios().post(url, data, config);
            console.log('RES: ', res);
            return success({
                enabled: res.data.enabled,
                id: res.data.id,
                name: res.data.name,
                username: res.data.username,
                role: res.data.authorities[0].authority,
                authorization: res.headers.Authorization
            });
        } catch (e) {
            return this.handleError(e);
        }
    }
}