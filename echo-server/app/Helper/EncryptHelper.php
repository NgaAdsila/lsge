<?php


namespace App\Helper;


class EncryptHelper
{
    private static $key = 'bHNnZUBUcnVzdE9yTGll';

    public static function encode($data) {
        return self::base64urlEncode(
            self::base64urlEncode(json_encode($data)) . self::base64urlEncode(self::$key));
    }

    public static function decode($json) {
        $keyEncoded = self::base64urlEncode(self::$key);
        $dataWithKeyEncoded = self::base64urlDecode($json);
        return json_decode(self::base64urlDecode(substr($dataWithKeyEncoded, 0, -strlen($keyEncoded))));
    }

    private static function base64urlEncode($data) {
        return rtrim(strtr(base64_encode($data), '+/', '-_'), '=');
    }

    private static function base64urlDecode($data) {
        return base64_decode(
            str_pad(strtr($data, '-_', '+/'), strlen($data) % 4, '=', STR_PAD_RIGHT));
    }
}
