import axios from 'axios'
import { RESPONSE, success, fail } from '../services/constants'
import store from '../store/index'
import i18n from '../plugins/i18n';

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
            return this.handleError(e.response);
        }
    },
    async post(url, data= {}, config = {}) {
        try {
            const res = await this.getAxios().post(url, data, config);
            return success(res.data);
        } catch (e) {
            return this.handleError(e.response);
        }
    },
    async put(url, data = {}, config = {}) {
        try {
            const res = await this.getAxios().put(url, data, config);
            return success(res.data);
        } catch (e) {
            return this.handleError(e.response);
        }
    },
    async remove(url, config = {}) {
        try {
            const res = await this.getAxios().delete(url, config);
            return success(res.data);
        } catch (e) {
            return this.handleError(e.response);
        }
    },
    async handleError(e) {
        if (!e || !e.data || !e.data.message) {
            return fail(i18n.t('common.validation.exception'), RESPONSE.CODE.EXCEPTION);
        }
        return fail(e.data.message, e.status);
    }
}