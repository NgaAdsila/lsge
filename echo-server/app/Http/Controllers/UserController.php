<?php


namespace App\Http\Controllers;

use App\Constants\ChannelEnum;
use App\Events\MainMessageEvent;
use App\Helper\EncryptHelper;
use App\Helper\ResponseHelper;
use App\Http\Requests\ChannelEventReq;
use App\Http\Requests\UserIdListRequest;
use App\Http\Requests\UserRoleListRequest;
use GuzzleHttp\Exception\ClientException;

class UserController extends Controller
{
    private $client;

    public function __construct(\GuzzleHttp\Client $client)
    {
        $this->client = $client;
    }

    public function band(UserIdListRequest $request)
    {
        try {
            $data = $request->only('userIds');
            $response = $this->client->put(config('services.api.domain') . '/users/band', [
                'headers' => [
                    'Authorization' => $request->header('Base-Authorization'),
                    'Accept' => 'application/json',
                    'Content-Type' => 'application/json'
                ],
                'json' => [
                    'userIds' => $data['userIds']
                ]
            ]);
            if ($response->getStatusCode() == ResponseHelper::HTTP_STATUS_OK) {
                event(new MainMessageEvent(EncryptHelper::encode(
                    new ChannelEventReq(ChannelEnum::EVENT_BAND_USER, ['userIds' => $data['userIds']])
                )));
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }

    public function resetPassword(UserIdListRequest $request)
    {
        try {
            $data = $request->only('userIds');
            $response = $this->client->put(config('services.api.domain') . '/users/reset-password', [
                'headers' => [
                    'Authorization' => $request->header('Base-Authorization'),
                    'Accept' => 'application/json',
                    'Content-Type' => 'application/json'
                ],
                'json' => [
                    'userIds' => $data['userIds']
                ]
            ]);
            if ($response->getStatusCode() == ResponseHelper::HTTP_STATUS_OK) {
                event(new MainMessageEvent(EncryptHelper::encode(
                    new ChannelEventReq(ChannelEnum::EVENT_RESET_PASSWORD, ['userIds' => $data['userIds']])
                )));
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }

    public function updateRole(UserRoleListRequest $request)
    {
        try {
            $data = $request->only('userRoleList');
            $response = $this->client->put(config('services.api.domain') . '/users/update-role', [
                'headers' => [
                    'Authorization' => $request->header('Base-Authorization'),
                    'Accept' => 'application/json',
                    'Content-Type' => 'application/json'
                ],
                'json' => [
                    'userRoleList' => $data['userRoleList']
                ]
            ]);
            if ($response->getStatusCode() == ResponseHelper::HTTP_STATUS_OK) {
                event(new MainMessageEvent(EncryptHelper::encode(
                    new ChannelEventReq(ChannelEnum::EVENT_UPDATE_ROLE_USER, ['userIds' => $data['userIds']])
                )));
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }
}
