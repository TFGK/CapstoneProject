import React, { Component } from 'react'
var RoadAPIs = require('./test.html');

class RoadAPI extends Component {
    componentDidMount () {
        const script = document.createElement("script");
    
        script.src = "./CC.js";
        script.async = true;
        document.body.appendChild(script);
    }

    render() {
        return (
        <div className="container">
            <h1>Test</h1>
            <div id="map_div" />
            <br /><br /><br />
            <hr />
            <br /><br /><br />
            <div id="map_div2" />
            <div id="divResult" style={{height: '200px', paddingLeft: '20px', overflow: 'auto'}} />
        </div>
        )
    }
}


export default RoadAPI
