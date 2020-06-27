<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Timeline extends Model
{
    protected $fillable = ['user_id', 'place_name', 'place_lat', "place_lng"];

    public function user()
    {
        return $this->belongsTo(User::class);
    }
