import React, { Component } from "react";
import { Grid, Row, Col } from "react-bootstrap";
import axios from 'axios';

import Location from '../components/location/Location';
import { StatsCard } from "components/StatsCard/StatsCard.jsx";
//import { } from "variables/Variables.jsx";
import Location_list from "components/location/Location_list";

class Dashboard extends Component {
  state = {
    location_datas: [],
    location_data: {},
    loader : false,
    url: "/api/locations",

    // count state
    cont1: 0,
    cont2: 0,
    cont3: 0,
  };

  componentDidMount() {
    this.getDatas();
  };

  //ObjCount 
  ObjCount = () => {
    // 신호등, 횡단보도, 버스정류장 갯수 확인
    const tt = this.state.location_datas;
    this.state.cont1 = 0;
    this.state.cont2 = 0;
    this.state.cont3 = 0;
    console.log('배열객체 확인', tt);
    for(let i=0; i<tt.length; i++) {
        if(tt[i].location_type == "신호등") {   
            this.state.cont1++;
        } else if (tt[i].location_type == "횡단보도") {
            this.state.cont2++;
        } else if (tt[i].location_type == "버스정류장") {
            this.state.cont3++;
        }
    }
    console.log('cont1', this.state.cont1);
    console.log('cont2', this.state.cont2);
    console.log('cont3', this.state.cont3);
  }

  // 조회
  getDatas = async() => {
    this.setState({ loader: true });
    const location_datas = await axios.get(this.state.url);
    this.setState({ location_datas: location_datas.data, loader: false });
  };

  // createLegend(json) {
  //   var legend = [];
  //   for (var i = 0; i < json["names"].length; i++) {
  //     var type = "fa fa-circle text-" + json["types"][i];
  //     legend.push(<i className={type} key={i} />);
  //     legend.push(" ");
  //     legend.push(json["names"][i]);
  //   }
  //   return legend;
  // }


  render() {
    return (
      <div className="content">
        <Grid fluid>
          {/* start card => 그 4개 창 나오는거 */}
          <Row>
            <Location
            mapStyles = {{
              width: '98%',
              height: '80vh',
              margin: "13% 0% 0% 0%"
            }}
          />
          </Row>
          <Row>
            <Col lg={3} sm={6}>
              <StatsCard
                bigIcon={<i className="pe-7s-server text-warning" />}
                statsText="신호등"
                statsValue={this.state.cont1}
                statsIcon={<i className="fa fa-refresh" />}
                statsIconText="Updated now"
              />
            </Col>

            <Col lg={3} sm={6}>
              <StatsCard
                bigIcon={<i className="pe-7s-wallet text-success" />}
                statsText="횡단보도"
                statsValue={this.state.cont2}
                statsIcon={<i className="fa fa-refresh" />}
                statsIconText="Last day"
              />
            </Col>

            <Col lg={3} sm={6}>
              <StatsCard
                bigIcon={<i className="pe-7s-graph1 text-danger" />}
                statsText="버스정류장"
                statsValue={this.state.cont3}
                statsIcon={<i className="fa fa-refresh" />}
                statsIconText="In the 1 hour"
              />
            </Col>

            <Col lg={3} sm={6}>
              <StatsCard
                bigIcon={<i className="pe-7s-server text-warning" />}
                statsText="합계 Data"
                statsValue={
                  this.state.cont1 +
                  this.state.cont2 +
                  this.state.cont3}
                statsIcon={<i className="fa fa-refresh" />}
                statsIconText="Updated now"
              />
            </Col>
          </Row>
        </Grid>
        {this.ObjCount()};
      </div>
    );
  }
}

export default Dashboard;
