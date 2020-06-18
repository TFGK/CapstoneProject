<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Location extends Model
{
    protected $fillable = [
        'location_name',
        'location_type',
        'location_lat',
        'location_lng'
    ];
}
