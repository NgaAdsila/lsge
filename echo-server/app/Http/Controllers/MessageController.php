<?php


namespace App\Http\Controllers;


use App\Events\CreateMessageEvent;
use App\Events\MainIsReadMessageEvent;
use App\Events\MainNewMessageEvent;
use App\Events\ReadMessageEvent;
use App\Helper\EncryptHelper;
use App\Helper\ResponseHelper;
use App\Http\Requests\CreateMessageRequest;
use GuzzleHttp\Exception\ClientException;
use Illuminate\Http\Request;

class MessageController extends Controller
{
    private $client;

    public function __construct(\GuzzleHttp\Client $client)
    {
        $this->client = $client;
    }

    public function createMessage(CreateMessageRequest $request) {
        try {
            $data = $request->only('message', 'chatroomId');
            $response = $this->client->post(config('services.api.domain') . '/chatrooms/create-message', [
                'headers' => [
                    'Authorization' => $request->header('Base-Authorization'),
                    'Accept' => 'application/json',
                    'Content-Type' => 'application/json'
                ],
                'json' => [
                    'chatroomId' => $data['chatroomId'],
                    'message' => $data['message']
                ]
            ]);
            if ($response->getStatusCode() == ResponseHelper::HTTP_STATUS_OK) {
                $body =  json_decode($response->getBody(), true);
                event(new CreateMessageEvent($data['chatroomId'], $body));
                event(new MainNewMessageEvent(EncryptHelper::encode($body)));
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }

    public function readMessage(Request $request, $id) {
        try {
            $response = $this->client->put(config('services.api.domain') . '/chatrooms/is-read-message/' . $id, [
                'headers' => [
                    'Authorization' => $request->header('Base-Authorization'),
                    'Accept' => 'application/json'
                ]
            ]);
            if ($response->getStatusCode() == ResponseHelper::HTTP_STATUS_OK) {
                $body =  json_decode($response->getBody(), true);
                event(new ReadMessageEvent($body['chatroomId'], $body));
                event(new MainIsReadMessageEvent(EncryptHelper::encode($body)));
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }
}
