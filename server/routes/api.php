<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\User;
use App\Timeline;

Route::resource('locations', 'Api\LocationController')->except(['create','edit']);

Route::post('register', 'UserController@register');
Route::post('login', 'UserController@login');
Route::get('profile', 'UserController@getAuthenticatedUser');

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});

//timeline
Route::get('/timelines', function(Request $request) {
    $email = "test@test.com";
    $id = User::where('email', $email)->pluck('id')->first();
    $timeline = Timeline::where('user_id', $id)->get();
    // return response()->json(compact('timeline'),201);
    return response()->json($timeline);
});

Route::get('RoadAPI', function() {
    return view('road');
});

// articles
Route::get('tasks', 'TaskController@index');
Route::get('task/{id}', 'TaskController@show');
Route::post('task', 'TaskController@store');
Route::put('task/{id}', 'TaskController@update');
Route::delete('task/{id}', 'TaskController@delete');