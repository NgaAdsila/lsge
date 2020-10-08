<?php


namespace App\Http\Controllers;


use App\Events\AddFriendEvent;
use App\Events\ApproveFriendEvent;
use App\Events\CancelFriendEvent;
use App\Helper\EncryptHelper;
use App\Helper\ResponseHelper;
use App\Http\Requests\AddingFriendRequest;
use App\Http\Requests\UpdatingRelationStatusRequest;
use GuzzleHttp\Exception\ClientException;

class RelationshipController extends Controller
{
    private $client;

    public function __construct(\GuzzleHttp\Client $client)
    {
        $this->client = $client;
    }

    public function addFriend(AddingFriendRequest $request) {
        try {
            $data = $request->only('recUserId');
            $response = $this->client->post(config('services.api.domain') . '/relationships', [
                'headers' => [
                    'Authorization' => $request->header('Base-Authorization'),
                    'Accept' => 'application/json',
                    'Content-Type' => 'application/json'
                ],
                'json' => [
                    'recUserId' => $data['recUserId']
                ]
            ]);
            if ($response->getStatusCode() == ResponseHelper::HTTP_STATUS_OK) {
                $user = $request->user();
                event(new AddFriendEvent(EncryptHelper::encode([
                    'reqUserId' => $user->id,
                    'recUserId' => $data['recUserId'],
                    'name' => $user->name
                ])));
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }

    public function approveFriend(UpdatingRelationStatusRequest $request) {
        try {
            $data = $request->only('userId');
            $response = $this->client->put(config('services.api.domain') . '/relationships/approve', [
                'headers' => [
                    'Authorization' => $request->header('Base-Authorization'),
                    'Accept' => 'application/json',
                    'Content-Type' => 'application/json'
                ],
                'json' => [
                    'userId' => $data['userId']
                ]
            ]);
            if ($response->getStatusCode() == ResponseHelper::HTTP_STATUS_OK) {
                $user = $request->user();
                event(new ApproveFriendEvent(EncryptHelper::encode([
                    'reqUserId' => $data['userId'],
                    'recUserId' => $user->id,
                    'name' => $user->name
                ])));
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }

    public function cancelFriend(UpdatingRelationStatusRequest $request) {
        try {
            $data = $request->only('userId');
            $response = $this->client->put(config('services.api.domain') . '/relationships/cancel', [
                'headers' => [
                    'Authorization' => $request->header('Base-Authorization'),
                    'Accept' => 'application/json',
                    'Content-Type' => 'application/json'
                ],
                'json' => [
                    'userId' => $data['userId']
                ]
            ]);
            if ($response->getStatusCode() == ResponseHelper::HTTP_STATUS_OK) {
                $user = $request->user();
                event(new CancelFriendEvent(EncryptHelper::encode([
                    'reqUserId' => $data['userId'],
                    'recUserId' => $user->id,
                    'name' => $user->name
                ])));
                return ResponseHelper::success(['OK']);
            }
        } catch (ClientException $e) {
            return ResponseHelper::fail(json_decode($e->getResponse()->getBody()->getContents()), $e->getCode());
        }
    }
}
