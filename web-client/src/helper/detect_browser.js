const data = {
    header: [navigator.platform, navigator.userAgent, navigator.appVersion, navigator.vendor, window.opera],
    os: [
        { name: 'Windows Phone', value: 'Windows Phone', version: 'OS' },
        { name: 'Windows', value: 'Win', version: 'NT' },
        { name: 'iPhone', value: 'iPhone', version: 'OS' },
        { name: 'iPad', value: 'iPad', version: 'OS' },
        { name: 'Kindle', value: 'Silk', version: 'Silk' },
        { name: 'Android', value: 'Android', version: 'Android' },
        { name: 'PlayBook', value: 'PlayBook', version: 'OS' },
        { name: 'BlackBerry', value: 'BlackBerry', version: '/' },
        { name: 'Macintosh', value: 'Mac', version: 'OS X' },
        { name: 'Linux', value: 'Linux', version: 'rv' },
        { name: 'Palm', value: 'Palm', version: 'PalmOS' }
    ],
    browser: [
        { name: 'Chrome', value: 'Chrome', version: 'Chrome' },
        { name: 'Firefox', value: 'Firefox', version: 'Firefox' },
        { name: 'Safari', value: 'Safari', version: 'Version' },
        { name: 'Internet Explorer', value: 'MSIE', version: 'MSIE' },
        { name: 'Opera', value: 'Opera', version: 'Opera' },
        { name: 'BlackBerry', value: 'CLDC', version: 'CLDC' },
        { name: 'Mozilla', value: 'Mozilla', version: 'Mozilla' }
    ]
};

function matchItem(string, data) {
    var i = 0,
        regex,
        regexv,
        match,
        version;

    for (i = 0; i < data.length; i += 1) {
        regex = new RegExp(data[i].value, 'i');
        match = regex.test(string);
        if (match) {
            regexv = new RegExp(data[i].version + '[- /:;]([\\d._]+)', 'i');
            version = string.match(regexv);
            if (version && version[1]) {
                version = version[1];
            }
            return {
                name: data[i].name,
                version: version || 0
            };
        }
    }
    return { name: 'unknown', version: 0 };
}

export function getOs() {
    return matchItem(data.header.join(' '), data.os);
}

export function getBrowser() {
    return matchItem(data.header.join(' '), data.browser);
}