<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Timeline;

class TimelineController extends Controller
{
    public function index()
    {
        $timeline = New Timeline();
        
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {

    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        $locations = New Timeline();
        $locations->location_name = $request->location_name;
        $locations->location_type = $request->location_type;
        $locations->location_lat = $request->location_lat;
        $locations->location_lng = $request->location_lng;
        $locations->save();

        return response()->json($locations);
        

    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        return Location::find($id);
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
    
}
