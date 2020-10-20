import {ALLOWED_IMAGE_FILE_EXTENSIONS, API_PATH} from './constants';
import ApiService from '../helper/ApiService';

export async function getList(req = {}) {
    return await ApiService.post(API_PATH.FILE_GET_LIST, req, {});
}

function getFileConfig(extension) {
    if (!extension || extension === '') {
        return {};
    }
    extension = extension.toLowerCase();
    return extension === 'pdf' || ALLOWED_IMAGE_FILE_EXTENSIONS.includes(extension) ? { responseType: 'arraybuffer' } :{};
}

export async function readFile(filePath = '', extension = '') {
    return await ApiService.get(`${API_PATH.FILE_READ}?filePath=${filePath}`, getFileConfig(extension));
}