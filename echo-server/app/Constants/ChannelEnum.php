<?php
namespace App\Constants;

class ChannelEnum
{
    const CHANNEL_MESSAGE = 'channel-message-';
    const CHANNEL_MAIN = 'channel-main-top';

    const EVENT_CREATE_MESSAGE = 'created-message';
    const EVENT_READ_MESSAGE = 'is-read-message';
    const EVENT_ADD_FRIEND = 'add-friend';
    const EVENT_APPROVE_FRIEND = 'approve-friend';
    const EVENT_CANCEL_FRIEND = 'cancel-friend';
}
