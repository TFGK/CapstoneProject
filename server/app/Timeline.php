<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Timeline extends Model
{
    protected $fillable = ['location_name', 'location_lat', "location_lng"];

    public function user()
    {
        return $this->belongsTo(User::class);
    }
}
