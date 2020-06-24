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
Route::get('task', 'Api\TaskController@index');
Route::get('task/{id}', 'Api\TaskController@show');
Route::post('task', 'Api\TaskController@store');
Route::put('task/{id}', 'Api\TaskController@update');
Route::delete('task/{id}', 'Api\TaskController@delete');