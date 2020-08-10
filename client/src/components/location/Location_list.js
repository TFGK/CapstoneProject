import React, { Component, createContext } from "react";
import Location_data from './Location_data';
import { Card } from "components/Card/Card.jsx";
import {
    Col,
  } from "react-bootstrap";

export default class Location_list extends Component {

    onDelete = id => {
        this.props.onDelete(id);
        //console.log('location list,', id);
    };

    onEdit = data => {
        this.props.onEdit(data);
        //console.log('location list,', data);
    };

    render() {
        const location_datas = this.props.location_datas;
        return (
            <Col md={8}>
                <Card
                    title="データリスト"
                    content={
                    <table className="ui celled table">
                    <thead>
                        <tr>
                            <th style={{width: '50px', textAlign:"center" }}>No.</th>
                            <th>データタイプ</th>
                            <th>場所名</th>
                            <th>緯度</th>
                            <th>経度</th>
                            <th style={{ width: "165px" }}>修正</th>
                        </tr>
                    </thead>

                    <tbody>
                        {
                        location_datas.map(location_data => {
                          return (
                            <Location_data 
                              location_data={location_data}
                              key={location_data.id}
                              onDelete={this.onDelete}
                              onEdit={this.onEdit}
                            />
                            );
                          })
                        }
                    </tbody>
                </table>
                }
                />
            </Col>
               
        );
    }
}
