import React, { Component } from "react";
import Button from "components/CustomButton/CustomButton.jsx";

export default class Location_data extends Component {
    onDelete = id  => {
        this.props.onDelete(this.props.location_data.id);
    }

    onEdit = () => {
        this.props.onEdit(this.props.location_data);
    }
    render() {
        const {id, location_name, location_type, location_lat, location_lng } = this.props.location_data;
        return (
            <tr>
                <td style={{ textAlign: "center" }}>{id}</td>
                <td>{location_type}</td>
                <td>{location_name}</td>
                <td>{location_lat}</td>
                <td>{location_lng}</td>
                <td>
                    <Button className="edited_button" onClick={this.onEdit}>Edit</Button>
                    <Button className="deleted_button" onClick={this.onDelete}>Delete</Button>
                </td>
            </tr>
        );
    }
}
