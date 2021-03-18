<?php

namespace App\Http\Controllers;

use App\Constants\ChannelEnum;
use App\Events\MainMessageEvent;
use App\Helper\EncryptHelper;
use App\Helper\ResponseHelper;
use App\Http\Requests\AddingPostRequest;
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

    public function createComment(CreatingCommentRequest $request, $id)
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
                return ResponseHelper::success($body);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }

    public function like(Request $request, $id)
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

    public function dislike(Request $request, $id)
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
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }

    public function add(AddingPostRequest $request)
    {
        try {
            $response = $this->client->post(config('services.api.domain') . '/posts', [
                'headers' => [
                    'Authorization' => $request->header('Base-Authorization'),
                    'Accept' => 'application/json',
                    'Content-Type' => 'application/json'
                ],
                'json' => $request->only('title', 'content', 'shareMode')
            ]);
            if ($response->getStatusCode() == ResponseHelper::HTTP_STATUS_OK) {
                $body =  json_decode($response->getBody(), true);
                $body['name'] = $request->user()->name;
                event(new MainMessageEvent(EncryptHelper::encode(
                    new ChannelEventReq(ChannelEnum::EVENT_NEW_POST, $body))));
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }
}
