<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Timeline extends Model
{
    protected $fillable = ['id', 'user_id', 'place_name', 'place_lat', "place_lng"];

}