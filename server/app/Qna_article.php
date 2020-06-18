<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Qna_article extends Model
{
    protected $fillable = [
        'qna_type',
        'title',
        'content',
    ];
}
