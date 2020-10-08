<?php


namespace App\Helper;


class ResponseHelper
{
    const HTTP_STATUS_OK = 200;
    const HTTP_STATUS_FAIL = 400;
    const HTTP_STATUS_NOTFOUND = 404;

    public static function success($data)
    {
        return response()->json($data, self::HTTP_STATUS_OK);
    }

    public static function fail($data, $status = self::HTTP_STATUS_FAIL)
    {
        return response()->json($data, $status);
    }
}
