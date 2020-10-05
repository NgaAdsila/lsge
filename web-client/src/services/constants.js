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

export const RELATION_STATUS = {
    PENDING: 'PENDING',
    APPROVED: 'APPROVED',
    CANCEL: 'CANCEL'
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
    AUTH_REFRESH_TOKEN: '/api/auth/refresh-token',
    AUTH_FORGET_PASSWORD: '/api/auth/forget-password',
    USER_ROLE: '/api/users/get-role',
    CURRENT_USER: '/api/users/current-user',
    USER_UPDATE: '/api/users/update',
    USER_CHANGE_PASSWORD: '/api/users/change-password',
    USER_GET_LIST: '/api/users/user-list',
    LOGIN_HISTORY_FILTER_BY_USER: '/api/login-histories/filter-by-user',
    RELATIONSHIP_ADD_FRIEND: '/api/relationships',
    RELATIONSHIP_CANCEL_FRIEND: '/api/relationships/cancel',
    RELATIONSHIP_APPROVE_FRIEND: '/api/relationships/approve',
    RELATIONSHIP_LIST_FRIEND: '/api/relationships/friend-list',
    CHATROOM_INIT_NORMAL: '/api/chatrooms/init-normal',
    CHATROOM_API: '/api/chatrooms',
    CHATROOM_IS_READ_MESSAGE: '/api/chatrooms/is-read-message',
    ECHO_CREATE_MESSAGE: '/api/create-message',
    ECHO_AUTH_LOGIN: '/api/login',
};

export const ECHO_API_URL = process.env.LARAVEL_ECHO_SERVER_API_URL || 'http://localhost:8000';

export const ECHO_CHANNEL = {
    CHANNEL_MAIN: 'channel-main-top',
    CHANNEL_MESSAGE: 'channel-message-'
}

export const ECHO_EVENT = {
    CREATE_MESSAGE: 'created-message',
    ADD_FRIEND: 'add-friend',
    APPROVE_FRIEND: 'approve-friend',
    CANCEL_FRIEND: 'cancel-friend'
}

export const VARIANT = {
    DEFAULT: '',
    PRIMARY: 'primary',
    SECONDARY: 'secondary',
    DANGER: 'danger',
    WARNING: 'warning',
    SUCCESS: 'success',
    INFO: 'info'
}

export const MESSAGE_TYPE = {
    NORMAL: 'NORMAL',
    DEFAULT: 'DEFAULT'
}