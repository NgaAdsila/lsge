<?php

namespace App\Events;

use App\Constants\ChannelEnum;
use Illuminate\Broadcasting\InteractsWithSockets;
use Illuminate\Broadcasting\PresenceChannel;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class ReadMessageEvent implements ShouldBroadcast
{
    use Dispatchable, InteractsWithSockets, SerializesModels;

    private $chatroomId;

    public $message;

    /**
     * Create a new event instance.
     *
     * @param $chatroomId
     * @param $message
     */
    public function __construct($chatroomId, $message)
    {
        $this->chatroomId = $chatroomId;
        $this->message = $message;
    }

    /**
     * Get the channels the event should broadcast on.
     *
     * @return \Illuminate\Broadcasting\Channel|array
     */
    public function broadcastOn()
    {
        return new PresenceChannel(ChannelEnum::CHANNEL_MESSAGE . $this->chatroomId);
    }

    /**
     * The event's broadcast name.
     *
     * @return string
     */
    public function broadcastAs()
    {
        return ChannelEnum::EVENT_READ_MESSAGE;
    }
}
