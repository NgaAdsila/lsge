<?php


namespace App\Http\Controllers;


use App\Helper\ResponseHelper;
use App\Services\AuthService;
use Illuminate\Http\Request;

class AuthController extends Controller
{

    private $authService;

    public function __construct()
    {
        $this->authService = new AuthService();
    }

    public function login(Request $request)
    {
        try {
            $user = $this->authService->getUser($request->bearerToken());
            if (!$user || empty($user)) {
                return ResponseHelper::fail(__('auth.failed'), ResponseHelper::HTTP_STATUS_NOTFOUND);
            }
            $newUser = $this->authService->createOrUpdateUser($user);
            $token = $this->authService->generateToken(config('app.url'), $newUser);
            return ResponseHelper::success([
                'token' => $token
            ]);
        } catch (\Exception $e) {
            return ResponseHelper::fail($e->getMessage());
        }
    }
}
