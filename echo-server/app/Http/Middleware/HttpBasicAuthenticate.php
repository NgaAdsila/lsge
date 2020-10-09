<?php

namespace App\Http\Middleware;

use Closure;

class HttpBasicAuthenticate
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
        $envs = [
            'dev',
            'staging',
            'production'
        ];
        if (in_array(app()->environment(), $envs) &&
            ($request->getUser() != env('BASIC_AUTH_USERNAME') ||
                $request->getPassword() != env('BASIC_AUTH_PASSWORD'))) {
            return response('Unauthorized', 401, ['WWW-Authenticate' => 'Basic']);
        }
        return $next($request);
    }
}
