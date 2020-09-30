export function parseJwt(token) {
    let base64Url = token.split('.')[1],
        base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'),
        jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map(function(c) {
                    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
                })
                .join('')
        );
    return JSON.parse(jsonPayload);
}

export function smartTime(time) {
    let d = new Date(time),
        now = new Date();

    const secondTime = Math.abs((now.getTime() - d.getTime()) / 1000);
    if (secondTime < 60) {
        return "Just now"
    }
    if (secondTime < 3600) {
        return `${Math.floor(secondTime / 60)} minutes ago`
    }
    if (secondTime < 86400) {
        return `${Math.floor(secondTime / 3600)} hours ago`
    }
    if (secondTime < 2592000) {
        return `${Math.floor(secondTime / 86400)} days ago`
    }

    let month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear(),
        hour = '' + d.getHours(),
        minute = '' + d.getMinutes(),
        second = '' + d.getSeconds();
    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;
    if (hour.length < 2)
        hour = '0' + hour;
    if (minute.length < 2)
        minute = '0' + minute;
    if (second.length < 2)
        second = '0' + second;

    return [day, month, year].join('/') + ' ' + [hour, minute, second].join(':');
}

export function randomColor() {
    return '#'+(Math.random()*0xFFFFFF<<0).toString(16)
}