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

        Route::group([
            'prefix' => 'relationships',
        ], function () {
            Route::post('', 'RelationshipController@addFriend');
            Route::put('approve', 'RelationshipController@approveFriend');
            Route::put('cancel', 'RelationshipController@cancelFriend');
        });

        Route::group([
            'prefix' => 'chatrooms',
        ], function () {
            Route::put('is-read-message/{id}', 'MessageController@readMessage');
            Route::put('', 'ChatroomController@update');
            Route::put('set-nickname', 'ChatroomController@setNickname');
            Route::put('auto-read-event', 'ChatroomController@autoReadMessageEvent');
        });

        Route::group([
            'prefix' => 'users',
        ], function () {
            Route::put('band', 'UserController@band');
            Route::put('reset-password', 'UserController@resetPassword');
            Route::put('update-role', 'UserController@updateRole');
        });

        Route::group([
            'prefix' => 'posts',
        ], function () {
            Route::post('{id}/create-comment', 'PostController@createComment');
            Route::post('{id}/like', 'PostController@like');
            Route::post('{id}/dislike', 'PostController@dislike');
            Route::post('', 'PostController@add');
        });
    });
});
