import axios from 'axios'
import {RESPONSE, success, fail, VARIANT} from '@/services/constants'
import store from '../store/index'
import i18n from '../plugins/i18n';
import ToastHelper from "@/helper/ToastHelper";
import { signOut } from "@/services/user_service";
import router from "@/router";

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
    getAxios(baseUrl = null, token = null) {
        instance.defaults.baseURL = baseUrl || URL;
        instance.defaults.headers.common['Authorization'] = 'Bearer ' + (token || store.getters.jwt);
        instance.defaults.headers.common['Base-Authorization'] = 'Bearer ' + store.getters.jwt;
        return instance;
    },
    async get(url, config = {}, baseUrl = null, token = null) {
        try {
            const res = await this.getAxios(baseUrl, token).get(encodeURI(url), config);
            return success(res.data);
        } catch (e) {
            return this.handleError(e.response);
        }
    },
    async post(url, data= {}, config = {}, baseUrl = null, token = null) {
        try {
            const res = await this.getAxios(baseUrl, token).post(url, data, config);
            return success(res.data);
        } catch (e) {
            return this.handleError(e.response);
        }
    },
    async put(url, data = {}, config = {}, baseUrl = null, token = null) {
        try {
            const res = await this.getAxios(baseUrl, token).put(url, data, config);
            return success(res.data);
        } catch (e) {
            return this.handleError(e.response);
        }
    },
    async remove(url, config = {}, baseUrl = null, token = null) {
        try {
            const res = await this.getAxios(baseUrl, token).delete(url, config);
            return success(res.data);
        } catch (e) {
            return this.handleError(e.response);
        }
    },
    async handleError(e) {
        if (!e || !e.data || !e.data.message ||
            e.status === RESPONSE.CODE.EXCEPTION || e.status === RESPONSE.CODE.FORBIDDEN) {
            return fail(i18n.t('common.validation.exception'), RESPONSE.CODE.EXCEPTION);
        }
        if (e.status === RESPONSE.CODE.UNAUTHORIZED && router.currentRoute.matched.some(r => r.meta.requiresAuth)) {
            await ToastHelper.message(i18n.t('common.validation.unauthorized'), VARIANT.DANGER)
            await signOut()
            return router.push({ name: 'Login' })
        } else {
            return fail(e.data.message, e.status);
        }
    }
}