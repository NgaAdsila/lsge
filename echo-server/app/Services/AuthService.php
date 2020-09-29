<?php


namespace App\Services;


use App\Helper\ResponseHelper;
use App\Models\UserMongo;
use GuzzleHttp\Client;
use GuzzleHttp\Exception\GuzzleException;
use Tymon\JWTAuth\Facades\JWTAuth;

class AuthService
{
    public function createOrUpdateUser($user)
    {
        $oldUser = UserMongo::query()->where('id', $user['id'])->first();
        if ($oldUser) {
            $oldUser->update([
                'name' => $user['name'],
                'email' => $user['email'],
                'lang_setting' => $user['lang_setting']
            ]);
            return $oldUser;
        }
        return UserMongo::query()->create([
            'id' => $user['id'],
            'name' => $user['name'],
            'email' => $user['email'],
            'lang_setting' => $user['lang_setting']
        ]);
    }

    public function generateToken($url, $user)
    {
        return JWTAuth::fromUser($user);
    }

    public function getUser(string $token)
    {
        try {
            $client = new Client();
            $response = $client->get(config('services.luc-manual.domain') . '/api/user', [
                'headers' => [
                    'Authorization' => 'Bearer ' . $token
                ]
            ]);
            if ($response->getStatusCode() == ResponseHelper::HTTP_STATUS_OK) {
                $body =  json_decode($response->getBody(), true);
                return $body['data'];
            }
        } catch (GuzzleException $e) {
            throw new \Exception($e);
        }
        return null;
    }
}
