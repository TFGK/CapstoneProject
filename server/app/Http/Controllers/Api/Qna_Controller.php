<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Qna_article;
use Illuminate\Http\Request;

class Qna_Controller extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $qnas = Qna_article::orderBy('id')->get();

        return response()->json($qnas);
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        $qnas = new Qna_article();
        $qnas->qna_type = $request->qna_type;
        $qnas->title = $request->title;
        $qnas->content = $request->content;
        $qnas->save();
        return response()->json($qnas);
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        return Qna_article::find($id);
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $qnas = Qna_article::findOrFail($id);
        $qnas->qna_type = $request->qna_type;
        $qnas->title = $request->title;
        $qnas->content = $request->content;
        $qnas->save();
        return response()->json($qnas);
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        //
    }
}
