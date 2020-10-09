export default {
    STORAGE_KEY: 'store',
    getValue: (key) => localStorage.getItem(key) ? JSON.parse(localStorage.getItem(key)) : null,
    setValue: (key, value) => localStorage.setItem(key, JSON.stringify(value)),
    removeByKey: (key) => localStorage.removeItem(key)
}