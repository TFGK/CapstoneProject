
import React, { Component } from 'react';
import axios from 'axios';

//구글맵 API Import
import Location from '../components/location/Location';
import Location_form from '../components/location/Location_form';
import Loader from '../components/location/Loader';
// import { isNamedExports } from 'typescript';

export default class Maps extends Component {
    state = {
        on:false,
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
            <div className="map_section">

              <div className="map_section_category">
                {this.ObjCount()}
                {this.state.loader ? <Loader /> : ""}
              </div>

              <div className="map_section_1">
                <Location_form
                  //datas는 list로감
                  location_datas={this.state.location_datas}
                  location_data={this.state.location_data}
                  onFormSubmit={this.onFormSubmit}
                  onDelete={this.onDelete}
                  onEdit={this.onEdit}
                />
              </div>
                                    
              <div className="map_section_right">
                <Location 
                    mapStyles = {{
                      width: '100%',
                      height: '80vh',
                      margin: "0% 0% 0% 0%"
                    }}
                />
              </div>

            </div>
          </div>
        );
    }
}

