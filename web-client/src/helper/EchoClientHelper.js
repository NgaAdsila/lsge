import Echo from "laravel-echo"
import Socketio from "socket.io-client"
import store from '../store/index'

export function initEcho() {
    return new Echo({
        broadcaster: 'socket.io',
        host: process.env.LARAVEL_ECHO_SERVER_URL || 'http://localhost:6001',
        transports: ['websocket', 'polling', 'flashsocket'],
        client: Socketio,
        auth: {
            headers: {
                Authorization: `Bearer ${store.getters.echoJwt}`,
            }
        },
        namespace: false
    })
}