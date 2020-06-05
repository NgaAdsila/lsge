export const RESPONSE = {
    STATUS: {
        SUCCESS: 0,
        ERROR: 1
    },
    CODE: {
        OK: 200,
        BAD_REQUEST: 400,
        NOT_FOUND: 404,
        UNAUTHORIZED: 401,
        FORBIDDEN: 403,
        EXCEPTION: 500
    }
};

export const PAGINATION = {
    LIMIT: 10,
    DEFAULT_PAGE: 1
};

export function success(data) {
    return {
        status: RESPONSE.STATUS.SUCCESS,
        code: RESPONSE.CODE.OK,
        data: data
    }
}

export function fail(message, code) {
    return {
        status: RESPONSE.STATUS.ERROR,
        code: code,
        message: message
    }
}

export const API_PATH = {
    AUTH_LOGIN: '/api/auth/signin',
    AUTH_SIGNUP: '/api/auth/signup',
    USER_ROLE: '/api/users/get-role',
    CURRENT_USER: '/api/users/current-user',
    USER_UPDATE: '/api/users/update',
    USER_CHANGE_PASSWORD: '/api/users/change-password',
    LOGIN_HISTORY_FILTER_BY_USER: '/api/login-histories/filter-by-user'
};