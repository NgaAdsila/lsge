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
    const EVENT_UPDATE_CHATROOM = 'update-chatroom';
    const EVENT_SET_NICKNAME = 'set-nickname';
    const EVENT_AUTO_READ = 'auto-read';
    const EVENT_BAND_USER = 'band-user';
    const EVENT_UPDATE_ROLE_USER = 'update-role';
    const EVENT_RESET_PASSWORD = 'reset-password';

    const EVENT_CREATE_POST_COMMENT = 'created-post-comment';

    const EVENT_MAIN_MESSAGE = 'message';
}
