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
    DEFAULT_PAGE: 1,
    MESSAGE_PER_PAGE: 100
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
    AUTH_INIT_RESET_PASSWORD: '/api/auth/init-reset-password',
    AUTH_RESET_PASSWORD: '/api/auth/reset-password',
    USER_ROLE: '/api/users/get-role',
    CURRENT_USER: '/api/users/current-user',
    USER_UPDATE: '/api/users/update',
    USER_CHANGE_PASSWORD: '/api/users/change-password',
    USER_GET_LIST: '/api/users/user-list',
    USER_FILTER: '/api/users/filter',
    USER_ROLE_OPTION: '/api/users/role-options',
    USER_BAND: '/api/users/band',
    USER_ACTIVE: '/api/users/active',
    USER_RESET_PASSWORD: '/api/users/reset-password',
    USER_UPDATE_ROLE: '/api/users/update-role',
    LOGIN_HISTORY_FILTER_BY_USER: '/api/login-histories/filter-by-user',
    RELATIONSHIP_ADD_FRIEND: '/api/relationships',
    RELATIONSHIP_CANCEL_FRIEND: '/api/relationships/cancel',
    RELATIONSHIP_APPROVE_FRIEND: '/api/relationships/approve',
    RELATIONSHIP_LIST_FRIEND: '/api/relationships/friend-list',
    CHATROOM_INIT_NORMAL: '/api/chatrooms/init-normal',
    CHATROOM_API: '/api/chatrooms',
    CHATROOM_IS_READ_MESSAGE: '/api/chatrooms/is-read-message',
    CHATROOM_SET_NICKNAME: '/api/chatrooms/set-nickname',
    CHATROOM_AUTO_READ_EVENT: '/api/chatrooms/auto-read-event',
    ECHO_CREATE_MESSAGE: '/api/create-message',
    ECHO_AUTH_LOGIN: '/api/login',
    FILE_GET_LIST: '/api/files/get-list',
    FILE_READ: '/api/files/read',
    POST_GET_LIST: '/api/posts/filter',
};

export const ECHO_API_URL = process.env.LARAVEL_ECHO_SERVER_API_URL || 'http://localhost:8000';

export const ECHO_CHANNEL = {
    CHANNEL_MAIN: 'channel-main-top',
    CHANNEL_MESSAGE: 'channel-message-'
}

export const ECHO_EVENT = {
    CREATE_MESSAGE: 'created-message',
    IS_READ_MESSAGE: 'is-read-message',
    UPDATE_CHATROOM: 'update-chatroom',
    SET_NICKNAME: 'set-nickname',
    ADD_FRIEND: 'add-friend',
    APPROVE_FRIEND: 'approve-friend',
    CANCEL_FRIEND: 'cancel-friend',
    AUTO_READ: 'auto-read',
    BAND_USER: 'band-user',
    RESET_PASSWORD_USER: 'reset-password',
    UPDATE_ROLE_USER: 'update-role',
    MESSAGE: 'message'
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

export const FILE_UPLOAD = {
    MAX_SIZE: 2097152,
    MAX_SIZE_TITLE: '2MB',
    ALLOWED_EXTENSION_REGEX: /(.gif)|(.png)|(.bmp)|(.jpeg)|(.jpg)|(.ico)$/,
    ALLOWED_EXTENSION_TITLE: 'GIF, PNG, BMP, JPEG, JPG or ICO'
}

export const TEXT_FILE_EXTENSIONS = [
    "txt", "md",
    "csv", "ssh", "pem", "pdf",
    "yml", "yaml", "properties",
    "asp", "aspx", "cer", "cfm", "cgi", "pl", "css", "scss", "sass", "htm", "html", "phtml",
    "js", "jsp", "part", "py", "rss", "xhtml", "json", "ts",
    "php", "vue", "java", "c", "class", "cpp", "h", "sh", "swift", "vb",
    "sql", "dat", "db", "dbf", "mdb", "log", "sav", "xml",
    "jpeg", "ico", "jpg", "png", "jpe", "jif", "jfif", "jfi", "gif"
]

export const ALLOWED_IMAGE_FILE_EXTENSIONS = [
    "jpeg", "ico", "jpg", "png", "jpe", "jif", "jfif", "jfi", "gif"
]

export const SELECTED_MODE = {
    NONE: 'none',
    SOME: 'some',
    ALL: 'all'
}

export const POST = {
    SHARE_MODE: {
        PRIVATE: 'PRIVATE',
        PUBLIC: 'PUBLIC',
        FRIEND: 'FRIEND'
    }
}