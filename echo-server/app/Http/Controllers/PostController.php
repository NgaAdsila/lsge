<?php

namespace App\Http\Controllers;

use App\Constants\ChannelEnum;
use App\Events\MainMessageEvent;
use App\Helper\EncryptHelper;
use App\Helper\ResponseHelper;
use App\Http\Requests\ChannelEventReq;
use App\Http\Requests\CreatingCommentRequest;
use GuzzleHttp\Exception\ClientException;
use Illuminate\Http\Request;

class PostController extends Controller
{
    private $client;

    public function __construct(\GuzzleHttp\Client $client)
    {
        $this->client = $client;
    }

    public function createComment($id, CreatingCommentRequest $request)
    {
        try {
            $data = $request->only('message');
            $response = $this->client->post(config('services.api.domain') . '/posts/' . $id . '/create-comment', [
                'headers' => [
                    'Authorization' => $request->header('Base-Authorization'),
                    'Accept' => 'application/json',
                    'Content-Type' => 'application/json'
                ],
                'json' => [
                    'message' => $data['message']
                ]
            ]);
            if ($response->getStatusCode() == ResponseHelper::HTTP_STATUS_OK) {
                $body =  json_decode($response->getBody(), true);
                $body['name'] = $request->user()->name;
                $body['commentedUserId'] = $request->user()->id;
                event(new MainMessageEvent(EncryptHelper::encode(
                    new ChannelEventReq(ChannelEnum::EVENT_CREATE_POST_COMMENT, $body))));
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }

    public function like($id, Request $request)
    {
        try {
            $response = $this->client->post(config('services.api.domain') . '/posts/' . $id . '/like', [
                'headers' => [
                    'Authorization' => $request->header('Base-Authorization'),
                    'Accept' => 'application/json',
                    'Content-Type' => 'application/json'
                ],
                'json' => []
            ]);
            if ($response->getStatusCode() == ResponseHelper::HTTP_STATUS_OK) {
                $body =  json_decode($response->getBody(), true);
                $body['name'] = $request->user()->name;
                $body['likedUserId'] = $request->user()->id;
                event(new MainMessageEvent(EncryptHelper::encode(
                    new ChannelEventReq(ChannelEnum::EVENT_LIKE_POST, $body))));
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }

    public function dislike($id, Request $request)
    {
        try {
            $response = $this->client->post(config('services.api.domain') . '/posts/' . $id . '/dislike', [
                'headers' => [
                    'Authorization' => $request->header('Base-Authorization'),
                    'Accept' => 'application/json',
                    'Content-Type' => 'application/json'
                ],
                'json' => []
            ]);
            if ($response->getStatusCode() == ResponseHelper::HTTP_STATUS_OK) {
                $body =  json_decode($response->getBody(), true);
                $body['name'] = $request->user()->name;
                $body['dislikedUserId'] = $request->user()->id;
                event(new MainMessageEvent(EncryptHelper::encode(
                    new ChannelEventReq(ChannelEnum::EVENT_DISLIKE_POST, $body))));
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }
}
