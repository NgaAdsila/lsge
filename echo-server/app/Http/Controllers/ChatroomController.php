<?php


namespace App\Http\Controllers;

use App\Events\SetNicknameEvent;
use App\Events\UpdateChatroomEvent;
use App\Helper\ResponseHelper;
use App\Http\Requests\SettingNicknameRequest;
use App\Http\Requests\UpdatingChatroomRequest;
use GuzzleHttp\Exception\ClientException;

class ChatroomController extends Controller
{
    private $client;

    public function __construct(\GuzzleHttp\Client $client)
    {
        $this->client = $client;
    }

    public function update(UpdatingChatroomRequest $request)
    {
        try {
            $data = $request->only('id', 'name');
            $response = $this->client->put(config('services.api.domain') . '/chatrooms/' . $data['id'], [
                'headers' => [
                    'Authorization' => $request->header('Base-Authorization'),
                    'Accept' => 'application/json',
                    'Content-Type' => 'application/json'
                ],
                'json' => [
                    'name' => $data['name']
                ]
            ]);
            if ($response->getStatusCode() == ResponseHelper::HTTP_STATUS_OK) {
                $body =  json_decode($response->getBody(), true);
                if ($body) {
                    event(new UpdateChatroomEvent($data['id'], $data['name'], $body));
                }
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }

    public function setNickname(SettingNicknameRequest $request)
    {
        try {
            $data = $request->only('id', 'userList');
            $response = $this->client->put(config('services.api.domain') . '/chatrooms/' . $data['id'] . '/set-nickname', [
                'headers' => [
                    'Authorization' => $request->header('Base-Authorization'),
                    'Accept' => 'application/json',
                    'Content-Type' => 'application/json'
                ],
                'json' => [
                    'userList' => $data['userList']
                ]
            ]);
            if ($response->getStatusCode() == ResponseHelper::HTTP_STATUS_OK) {
                $body =  json_decode($response->getBody(), true);
                if ($body) {
                    event(new SetNicknameEvent($data['id'], $data['userList'], $body));
                }
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }
}
