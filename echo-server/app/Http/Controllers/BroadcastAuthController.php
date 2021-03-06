<?php


namespace App\Http\Controllers;

use Illuminate\Http\Request;

class BroadcastAuthController extends Controller
{
    public function auth(Request $request)
    {
        $user = $request->user();
        $lastLogin = $user->lastLogin();
        $channel = $request->get('channel_name');
        return [
            'channel' => $channel,
            'channel_data' => [
                'user_id' => $user->id,
                'user_info' => [
                    'id' => $user->id,
                    'name' => $user->name,
                    'lastLogin' => $lastLogin ? (new \DateTime($lastLogin->created_at))->getTimestamp() : ''
                ]
            ]
        ];
    }
}
