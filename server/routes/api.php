<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

Route::resource('locations', 'Api\LocationController')->except(['create','edit']);

Route::post('register', 'Api\Auth\UserController@register');
Route::post('login', 'Api\Auth\UserController@login');
Route::put('update', 'Api\Auth\UserController@update');
Route::get('profile', 'Api\Auth\UserController@getAuthenticatedUser');

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
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