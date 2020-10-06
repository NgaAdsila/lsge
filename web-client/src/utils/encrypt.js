const b64c = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
const key = 'bHNnZUBUcnVzdE9yTGll';

function base64Encode(input) {
    let output = "";
    let chr1, chr2, chr3, enc1, enc2, enc3, enc4;
    let i = 0;

    input = utf8Encode(input);

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

function utf8Encode(string) {
    string = string.replace(/\r\n/g,"\n");
    let utfText = "";

    for (let n = 0; n < string.length; n++) {

        let c = string.charCodeAt(n);

        if (c < 128) {
            utfText += String.fromCharCode(c);
        }
        else if((c > 127) && (c < 2048)) {
            utfText += String.fromCharCode((c >> 6) | 192);
            utfText += String.fromCharCode((c & 63) | 128);
        }
        else {
            utfText += String.fromCharCode((c >> 12) | 224);
            utfText += String.fromCharCode(((c >> 6) & 63) | 128);
            utfText += String.fromCharCode((c & 63) | 128);
        }

    }

    return utfText;
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
    return utf8Decode(output).replace(/\u0000/g,'');
}

function utf8Decode(utfText) {
    let string = "";
    let i = 0;
    let c = 0, c2 = 0, c3 = 0;

    while ( i < utfText.length ) {

        c = utfText.charCodeAt(i);

        if (c < 128) {
            string += String.fromCharCode(c);
            i++;
        }
        else if((c > 191) && (c < 224)) {
            c2 = utfText.charCodeAt(i+1);
            string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
            i += 2;
        }
        else {
            c2 = utfText.charCodeAt(i+1);
            c3 = utfText.charCodeAt(i+2);
            string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
            i += 3;
        }

    }
    return string;
}

function base64UrlDecode($encoded = '') {
    return base64Decode($encoded.replace('-_', '+/').padEnd($encoded.length % 4, '='));
}

export function decode($input) {
    let keyEncoded = base64UrlEncode(key);
    let dataWithKeyEncoded = base64UrlDecode($input);
    return JSON.parse(base64UrlDecode(dataWithKeyEncoded
        .substr(0, dataWithKeyEncoded.length - keyEncoded.length)));
}