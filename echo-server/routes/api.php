<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::group([
    'middleware' => ['locale']
], function () {
    Route::post('login', 'AuthController@login');

    Route::group([
        'middleware' => ['jwt.auth'],
    ], function () {
        Route::post('broadcasting/auth', 'BroadcastAuthController@auth');
        Route::post('create-message', 'MessageController@createMessage');
        Route::post('relationships', 'RelationshipController@addFriend');
        Route::put('relationships/approve', 'RelationshipController@approveFriend');
        Route::put('relationships/cancel', 'RelationshipController@cancelFriend');
        Route::put('chatrooms/is-read-message/{id}', 'MessageController@readMessage');
        Route::put('chatrooms', 'ChatroomController@update');
        Route::put('chatrooms/set-nickname', 'ChatroomController@setNickname');
        Route::put('chatrooms/auto-read-event', 'ChatroomController@autoReadMessageEvent');
        Route::put('users/band', 'UserController@band');
        Route::put('users/reset-password', 'UserController@resetPassword');
        Route::put('users/update-role', 'UserController@updateRole');
    });
});
