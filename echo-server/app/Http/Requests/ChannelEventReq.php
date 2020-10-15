<?php


namespace App\Http\Requests;


class ChannelEventReq
{
    public $type;

    public $data;

    public function __construct($type, $data)
    {
        $this->type = $type;
        $this->data = $data;
    }
}
