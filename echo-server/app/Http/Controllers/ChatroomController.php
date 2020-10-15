<?php


namespace App\Http\Controllers;

use App\Constants\ChannelEnum;
use App\Events\ChatMessageEvent;
use App\Events\MainMessageEvent;
use App\Helper\EncryptHelper;
use App\Helper\ResponseHelper;
use App\Http\Requests\ChannelEventReq;
use App\Http\Requests\SettingNicknameRequest;
use App\Http\Requests\UpdatingChatroomRequest;
use GuzzleHttp\Exception\ClientException;
use Illuminate\Http\Request;

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
                event(new ChatMessageEvent($data['id'],
                    EncryptHelper::encode(new ChannelEventReq(ChannelEnum::EVENT_UPDATE_CHATROOM, [
                        'chatroomName' => $data['name'],
                        'message' => $body
                    ]))
                ));
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
                event(new ChatMessageEvent($data['id'],
                    EncryptHelper::encode(new ChannelEventReq(ChannelEnum::EVENT_SET_NICKNAME, [
                        'userList' => $data['userList'],
                        'message' => $body
                    ]))
                ));
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }

    public function autoReadMessageEvent(Request $request)
    {
        $user = $request->user();
        event(new MainMessageEvent(EncryptHelper::encode(
            new ChannelEventReq(ChannelEnum::EVENT_AUTO_READ, [
                'chatroomId' => $request->get('chatroomId'),
                'userId' => $user->id
            ])
        )));
        return ResponseHelper::success(['OK']);
    }
}
