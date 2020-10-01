<?php

namespace App\Events;

use App\Constants\ChannelEnum;
use Illuminate\Broadcasting\InteractsWithSockets;
use Illuminate\Broadcasting\PresenceChannel;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class CancelFriendEvent implements ShouldBroadcast
{
    use Dispatchable, InteractsWithSockets, SerializesModels;

    public $reqUserId;
    public $recUserId;
    public $name;

    /**
     * Create a new event instance.
     *
     * @param $reqUserId
     * @param $recUserId
     * @param $name
     */
    public function __construct($reqUserId, $recUserId, $name)
    {
        $this->reqUserId = $reqUserId;
        $this->recUserId = $recUserId;
        $this->name = $name;
    }

    /**
     * Get the channels the event should broadcast on.
     *
     * @return \Illuminate\Broadcasting\Channel|array
     */
    public function broadcastOn()
    {
        return new PresenceChannel(ChannelEnum::CHANNEL_MAIN);
    }

    /**
     * The event's broadcast name.
     *
     * @return string
     */
    public function broadcastAs()
    {
        return ChannelEnum::EVENT_CANCEL_FRIEND;
    }
}
