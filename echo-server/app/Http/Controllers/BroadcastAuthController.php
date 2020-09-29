<?php


namespace App\Http\Controllers\Api;


use App\Http\Controllers\Controller;
use Illuminate\Http\Request;

class BroadcastAuthController extends Controller
{
    public function auth(Request $request)
    {
        $user = $request->user();
        $channel = $request->get('channel_name');
        $this->addCurrentChannel($user, $channel);
        return [
            'channel' => $channel,
            'channel_data' => [
                'user_id' => $user->id,
                'user_info' => $user
            ]
        ];
    }

    private function addCurrentChannel($user, $channel)
    {
        if ($user) {
            $user->update([
                'current_channel' => $channel
            ]);
        }
    }
}
