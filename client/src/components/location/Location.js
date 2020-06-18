import React, { Component } from 'react';
import axios from 'axios';
import { Spring } from 'react-spring/renderprops'
//구글맵 API Import
import Maps from './Maps';

import traffic from '../images/traffic_light.png';
import crosswalk from '../images/crosswalk.png';
import busstop from '../images/bus_stop.png';

import '../css/location.css';

import Location_form from './Location_form';
import Location_list from './Location_list';
import Loader from '../Loader';

export default class Location extends Component {
    state = {
        on:false,
        location_datas: [],
        location_data: {},
        loader : false,
        url: "api/locations",

        // count state
        cont1: 0,
        cont2: 0,
        cont3: 0,
    };
    
    componentDidMount() {
        this.getDatas();
    };

    // 생성
    createDatas = async data => {
        this.setState({ loader: true });

        await axios.post(this.state.url, {
            location_name: data.location_name,
            location_type: data.location_type,
            location_lat: data.location_lat,
            location_lng: data.location_lng,
        });

        this.getDatas();
        this.ObjCount();
    };
    
    // 조회
    getDatas = async() => {
        this.setState({ loader: true });
        const location_datas = await axios.get(this.state.url);
        this.setState({ location_datas: location_datas.data, loader: false });
    };

    editDatas = async data => {
        // clear datas obj
        this.setState({ location_data: {}, loader: true });

        await axios.put(`${this.state.url}/${data.id}`, {
            location_name: data.location_name,
            location_type: data.location_type,
            location_lat: data.location_lat,
            location_lng: data.location_lng,
        });
        this.getDatas();
    };

    //삭제
    deleteDatas = async id => {
        this.setState({ loader: true });
        await axios.delete(`${this.state.url}/${id}`);

        this.getDatas();
    };

    onFormSubmit = data => {
        if(data.isEdit) {
            //is edit true
            this.editDatas(data);
        } else {
            //is eidt false
            this.createDatas(data);
        }
    };

    onDelete = id => {
        this.deleteDatas(id);
    }

    onEdit = data => {
        this.setState({ location_data: data });
        this.setState({
            on:!this.state.on
        })
    }

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

    render() {
        return (
            <div className="location">
              <div className="blackline" />
                <div className="map_section">
                  <div className="map_section_left">
                    <Location_form
                      location_data={this.state.location_data}
                      onFormSubmit={this.onFormSubmit}
                    />
                    <div className="map_section_category">
                    {this.ObjCount()}
                    
                      <div className="child category_section">
                        <img src={traffic} className="category_img"></img>
                        <p>신호등</p>
                        <Spring
                          from={{ number :0 }}
                          to={{number:this.state.cont1}}
                          delay={200}
                        >
                          {props => <div>{props.number}</div>}
                        </Spring>
                        <br />
                        <button>신호등</button>
                      </div>

                      <div className="child category_section">
                        <img src={crosswalk} className="category_img"></img>
                        <p>횡단보도</p>
                        <Spring
                          from={{ number :0 }}
                          to={{number:this.state.cont2}}
                          delay={200}
                        >
                          {props => <div>{props.number}</div>}
                        </Spring>
                        <br />
                        <button>횡단보도</button>
                      </div>
                      
                      <div className="child category_section">
                        <img src={busstop} className="category_img"></img>
                        <p>버스정류장</p>
                        <Spring
                          from={{ number :0 }}
                          to={{number:this.state.cont3}}
                          delay={200}
                        >
                          {props => <div>{props.number}</div>}
                        </Spring>
                        <br />
                        <button>버스정류장</button>
                      </div>
                    </div>

                    {/* <Location_list
                        location_datas={this.state.location_datas}
                        onDelete={this.onDelete}
                        onEdit={this.onEdit}
                    /> */}
                  </div>
                                    
                  <div className="map_section_right">
                    {this.state.loader ? <Loader /> : ""}
                    <Maps />
                  </div>
                    
                </div>
            </div>
        );
    }
}

