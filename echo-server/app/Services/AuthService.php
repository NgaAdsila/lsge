<?php

namespace App\Services;

use App\Models\User;
use Tymon\JWTAuth\Facades\JWTAuth;

class AuthService
{
    public function getUser(string $bearerToken)
    {
        if (!$bearerToken) {
            return null;
        }
        $payloadEncoded = explode('.', $bearerToken)[1];
        $payload = json_decode(base64_decode($payloadEncoded), true);
        return User::query()->find($payload['sub']);
    }

    public function generateToken($user)
    {
        return JWTAuth::fromUser($user);
    }
}
