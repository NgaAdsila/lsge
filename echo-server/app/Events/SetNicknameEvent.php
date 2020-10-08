<?php

namespace App\Events;

use App\Constants\ChannelEnum;
use Illuminate\Broadcasting\InteractsWithSockets;
use Illuminate\Broadcasting\PresenceChannel;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class SetNicknameEvent implements ShouldBroadcast
{
    use Dispatchable, InteractsWithSockets, SerializesModels;

    private $id;

    public $userList;

    public $message;

    /**
     * Create a new event instance.
     *
     * @return void
     */
    public function __construct($id, $userList, $message)
    {
        $this->id = $id;
        $this->userList = $userList;
        $this->message = $message;
    }

    /**
     * Get the channels the event should broadcast on.
     *
     * @return \Illuminate\Broadcasting\Channel|array
     */
    public function broadcastOn()
    {
        return new PresenceChannel(ChannelEnum::CHANNEL_MESSAGE . $this->id);
    }

    /**
     * The event's broadcast name.
     *
     * @return string
     */
    public function broadcastAs()
    {
        return ChannelEnum::EVENT_SET_NICKNAME;
    }
}
