import React, { Component } from "react";


export default class Location_data extends Component {
    onDelete = id  => {
        //console.log('data delete');
        this.props.onDelete(this.props.location_data.id);
    }

    onEdit = () => {
        this.props.onEdit(this.props.location_data);
    }
    render() {
        const {id, location_name, location_type, location_lat, location_lng } = this.props.location_data;
        console.log(this.props.location_data);
        return (
            <tr>
                <td style={{ textAlign: "center" }}>{id}</td>
                <td>{location_name}</td>
                <td>{location_type}</td>
                <td>{location_lat}</td>
                <td>{location_lng}</td>
                <td>
                    <button className="mini ui blue button" onClick={this.onEdit}>Edit</button>
                    <button className="mini ui red button deleted_button" onClick={this.onDelete}>Delete</button>
                </td>
            </tr>
        );
    }
}
