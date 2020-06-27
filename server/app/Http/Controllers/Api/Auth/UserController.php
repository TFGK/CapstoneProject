<?php

namespace App\Http\Controllers\Api\Auth;

use App\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;
use App\Http\Controllers\Controller;


//use JWTAuth;
use Tymon\JWTAuth\Facades\JWTAuth;
use Tymon\JWTAuth\Facades\JWTFactory;
use Tymon\JWTAuth\Exceptions\JWTException;
use Tymon\JWTAuth\Contracts\JWTSubject;
use Tymon\JWTAuth\PayloadFactory;
use Tymon\JWTAuth\JWTManager as JWT;

class UserController extends Controller
{   
   
    public function login(Request $request)
    {
        $credentials = $request->json()->all();

        try {
            if (!$token = JWTAuth::attempt($credentials)) {
                return response()->json(['error' => 'invalid_credentials'], 400);
            }
        } catch (JWTException $e) {
            return response()->json(['error' => 'could_not_create_token'], 500);
        }

        return response()->json( compact('token') );
    }

    public function register(Request $request)
    {
        
        $validator = Validator::make($request->json()->all() , [
            'name' => 'required|string|max:255',
            'email' => 'required|string|email|max:255|unique:users',
            'password' => 'required|string|min:4', 
        ]);

        if($validator->fails()){
                return response()->json($validator->errors()->toJson(), 400);
        }

        $user = User::create([
            'name' => $request->json()->get('name'),
            'email' => $request->json()->get('email'),
            'password' => Hash::make($request->json()->get('password')),
            'birthday' => $request->json()->get('birthday'),
            'city' => $request->json()->get('city'),
            'country' => $request->json()->get('country'),
            'about' => $request->json()->get('about'), 
            'address' => $request->json()->get('address'),
        ]);

        $token = JWTAuth::fromUser($user);

        return response()->json(compact('user','token'),201);
    }

    // public function refresh() 
    // {
    //     try {
    //         $newToken = auth()->refresh();
    //     } catch (\Tymon\JWTAuth\Exceptions\TokenInvalidException $e) {
    //         return response()->json(['error' => $e->getMessage()], 401);
    //     }

    //     return response()->json(['token' => $newToken]);

    // }

    public function update(Request $request)
    {
        $id = $request->json()->get('email');
        $olduser = User::whereEmail($id)->firstOrFail();

        $validator = Validator::make($request->json()->all() , [
            'name' => 'required|string|max:255',
            'email' => 'required|string|email|max:255|unique:users',
            'password' => 'required|string|min:4', 
        ]);

        if($validator->fails()){
                return response()->json($validator->errors()->toJson(), 400);
        }

        $data = [
            'name' => $request->json()->get('name'),
            'email' => $request->json()->get('email'),
            'password' => Hash::make($request->json()->get('password')),
            'birthday' => $request->json()->get('birthday'),
            'city' => $request->json()->get('city'),
            'country' => $request->json()->get('country'),
            'about' => $request->json()->get('about'), 
            'address' => $request->json()->get('address'),
        ];

        $user = $olduser->update($data);
        $token = JWTAuth::fromUser($user);
        return response()->json(compact('user','token'),201);
    }


    public function getAuthenticatedUser()
    {
        try {
            if (!$user = JWTAuth::parseToken()->authenticate()) {
                return response()->json(['user_not_found'], 404);
            }
        } catch (Tymon\JWTAuth\Exceptions\TokenExpiredException $e) {
            return response()->json(['token_expired'], $e->getStatusCode());
        } catch (Tymon\JWTAuth\Exceptions\TokenInvalidException $e) {
            return response()->json(['token_invalid'], $e->getStatusCode());
        } catch (Tymon\JWTAuth\Exceptions\JWTException $e) {
            return response()->json(['token_absent'], $e->getStatusCode());
        }
        return response()->json(compact('user'));
    }

}
