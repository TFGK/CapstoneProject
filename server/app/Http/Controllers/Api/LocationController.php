<?php

namespace App\Http\Controllers\Api;

use App\Location;
use App\Http\Controllers\Controller;
use Illuminate\Http\Request;

class LocationController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $locations = Location::orderBy('id')->get();

        return response()->json($locations);
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
        $locations = New Location();
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
    public function update(Request $request, $id)
    {
        $locations = Location::findOrFail($id);
        $locations->location_name = $request->location_name;
        $locations->location_type = $request->location_type;
        $locations->location_lat = $request->location_lat;
        $locations->location_lng = $request->location_lng;
        $locations->save();
        return response()->json($locations);
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $locations = Location::findOrFail($id);
        $locations->delete();

        return response()->json($locations);
    }
}
