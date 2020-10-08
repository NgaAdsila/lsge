<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Contracts\Auth\Guard;

class LocaleMiddleware
{
    /**
     * The Guard implementation.
     *
     * @var Guard
     */
    protected $auth;
    /**
     * Create a new filter instance.
     *
     * @param  Guard  $auth
     * @return void
     */
    public function __construct(Guard $auth)
    {
        $this->auth = $auth;
    }

    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
        $local = 'en';
        $user = $this->auth->user();
        $currentLocal = $request->header('local');
        if ($currentLocal) {
            $local = $currentLocal;
        } elseif ($user && isset($user->lang_setting)) {
            $local = $user->lang_setting;
        }
        app()->setLocale($local);
        return $next($request);
    }
}
