import i18n from "../plugins/i18n";

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

export function randDarkColor() {
    let lum = -0.25;
    let hex = String('#' + Math.random().toString(16).slice(2, 8)
        .toUpperCase()).replace(/[^0-9a-f]/gi, '');
    if (hex.length < 6) {
        hex = hex[0] + hex[0] + hex[1] + hex[1] + hex[2] + hex[2];
    }
    let rgb = "#",
        c, i;
    for (i = 0; i < 3; i++) {
        c = parseInt(hex.substr(i * 2, 2), 16);
        c = Math.round(Math.min(Math.max(0, c + (c * lum)), 255)).toString(16);
        rgb += ("00" + c).substr(c.length);
    }
    return rgb;
}

export function smartDisplayUsername(usernames = []) {
    if (usernames.length <= 2) {
        return usernames.join(', ')
    }
    return i18n.t('common.label.display_username', {
        name: `${usernames[0]}, ${usernames[1]}`,
        count: usernames.length - 2
    })
}