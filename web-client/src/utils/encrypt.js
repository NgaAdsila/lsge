const b64c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
const key = 'bHNnZUBUcnVzdE9yTGll';

function base64Encode(input) {
    let output = "";
    let chr1, chr2, chr3, enc1, enc2, enc3, enc4;
    let i = 0;

    input = unescape(encodeURIComponent(input));

    while (i < input.length) {

        chr1 = input.charCodeAt(i++);
        chr2 = input.charCodeAt(i++);
        chr3 = input.charCodeAt(i++);

        enc1 = chr1 >> 2;
        enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
        enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
        enc4 = chr3 & 63;

        if (isNaN(chr2)) {
            enc3 = enc4 = 64;
        } else if (isNaN(chr3)) {
            enc4 = 64;
        }

        output = output +
            b64c.charAt(enc1) + b64c.charAt(enc2) +
            b64c.charAt(enc3) + b64c.charAt(enc4);

    }

    return output;
}

function base64UrlEncode($data) {
    return base64Encode($data).replace('+/', '-_').replace(/=+$/, '');
}

function base64Decode(input) {
    let output = "";
    let chr1, chr2, chr3;
    let enc1, enc2, enc3, enc4;
    let i = 0;

    input = input.replace(/[^A-Za-z0-9+/=]/g, "");

    while (i < input.length) {

        enc1 = b64c.indexOf(input.charAt(i++));
        enc2 = b64c.indexOf(input.charAt(i++));
        enc3 = b64c.indexOf(input.charAt(i++));
        enc4 = b64c.indexOf(input.charAt(i++));

        chr1 = (enc1 << 2) | (enc2 >> 4);
        chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
        chr3 = ((enc3 & 3) << 6) | enc4;

        output = output + String.fromCharCode(chr1);

        if (enc3 !== 64) {
            output = output + String.fromCharCode(chr2);
        }
        if (enc4 !== 64) {
            output = output + String.fromCharCode(chr3);
        }

    }
    return decodeURIComponent(escape(output));
}

function base64UrlDecode(encoded = '') {
    encoded = encoded
        .replace(/-/g, '+')
        .replace(/_/g, '/');
    let pad = encoded.length % 4;
    if (pad) {
        if(pad === 1) {
            return '';
        }
        encoded += new Array(5-pad).join('=');
    }
    return base64Decode(encoded);
}

export function decode($input) {
    let keyEncoded = base64UrlEncode(key);
    let dataWithKeyEncoded = base64UrlDecode($input);
    return JSON.parse(base64UrlDecode(dataWithKeyEncoded
        .substr(0, dataWithKeyEncoded.length - keyEncoded.length)));
}