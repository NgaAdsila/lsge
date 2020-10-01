<?php


namespace App\Http\Controllers;


use App\Events\CreateMessageEvent;
use App\Helper\ResponseHelper;
use App\Http\Requests\CreateMessageRequest;
use GuzzleHttp\Client;
use GuzzleHttp\Exception\ClientException;

class MessageController extends Controller
{
    public function createMessage(CreateMessageRequest $request) {
        try {
            $data = $request->only('message', 'chatroomId');
            $client = new Client();
            $response = $client->post(config('services.api.domain') . '/chatrooms/create-message', [
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
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }
}
