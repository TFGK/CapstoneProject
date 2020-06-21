import React, { Component } from "react";
import { Card } from "components/Card/Card.jsx";
import { FormInputs } from "components/FormInputs/FormInputs.jsx";
import Button from "components/CustomButton/CustomButton.jsx";

import {
  Grid,
  Row,
  Col,
  FormGroup,
  ControlLabel,
  FormControl
} from "react-bootstrap";

import Location_list from './Location_list';

class Location_form extends Component {
    state = {
        form: {
               location_name: "",
               location_type: "",
               location_lat: "", 
               location_lng: "",
               isEdit : false
            },
        btnName: "저장",
        btnClass: "ui primary button submit-button"
    };

    isEmpty(obj) {
        return Object.entries(obj).length === 0 && obj.constructor === Object;
    };

    componentDidUpdate(prevProps) {
        if(prevProps !== this.props && !this.isEmpty(this.props.location_data)) {
            this.setState({
                form: { ...this.props.location_data, isEdit: true },
                btnName: "Update",
                btnClass: "ui orange button submit-button"

            })
            console.log("update");
        }
    };
    
    onFormSubmit = event => {
        // prevent form submit
        event.preventDefault();

        if(this.formValidation() ) {
            // send form data to location
            this.props.onFormSubmit(this.state.form);
        };

        // change the button to save 
        this.setState({
            btnName: "저장",
            btnClass: "ui primary button submit-button"
        });

        // clear form Fields
        this.clearFormFields();
    };

    //폼태그 유효성 검사 함수
    formValidation = () => {
        // location name
        if(document.getElementsByName("location_name")[0].value === "") {
            alert("Enter location name");
            return false;
        }

        // location explain
        if(document.getElementsByName("location_type")[0].value === "") {
            alert("Enter location explain");
            return false;
        }

        // location lat
        if(document.getElementsByName("location_lat")[0].value === "") {
            alert("Enter location lat");
            return false;
        }

        // location lng
        if(document.getElementsByName("location_lng")[0].value === "") {
            alert("Enter location lng");
            return false;
        }
        return true;
    };

    clearFormFields = () => {
        // change form state
        this.setState({
            form: {
                location_name: "",
                location_type: "",
                location_lat: "", 
                location_lng: "",
                isEdit : false
            }}
        );
        //clear form fields
        document.querySelector(".map_form").reset();
    };

    handleChange = event => {
        if(this.state.form.isEdit == true) {
            const { name, value } = event.target;
            let form = this.state.form;
            form[name] = value;
            this.setState({ form });
        } else {
            let form = {
                location_name: document.getElementsByName("location_name")[0].value,
                location_type: document.getElementsByName("location_type")[0].value,
                location_lat: document.getElementsByName("location_lat")[0].value, 
                location_lng: document.getElementsByName("location_lng")[0].value,
            }
            this.setState({ form });
        }
    };

    render() {
        return (
            <div className="content">
            <Grid fluid>
            <Row>
                <Col md={4}>
                <Card
                    title="data insert form"
                    content={
                    <form className="map_form">
                        <FormInputs
                        ncols={["col-md-6", "col-md-6"]}
                        properties={[
                            {
                            label: "Data type",
                            type: "text",
                            name: "location_type",
                            bsClass: "form-control",
                            placeholder: "Data type",
                            value: this.state.form.location_type,
                            onChange: this.handleChange
                            },
                            {
                            label: "location name",
                            type: "text",
                            name: "location_name",
                            bsClass: "form-control",
                            placeholder: "location name",
                            value: this.state.form.location_name,
                            onChange: this.handleChange
                            }
                        ]}
                        />
                        <FormInputs
                        ncols={["col-md-6", "col-md-6"]}
                        properties={[
                            {
                            label: "location_lat",
                            type: "text",
                            name: "location_lat",
                            bsClass: "form-control",
                            placeholder: "location_lat",
                            value: this.state.form.location_lat,
                            onChange: this.handleChange
                            },
                            {
                            label: "location_lng",
                            type: "text",
                            name: "location_lng",
                            bsClass: "form-control",
                            placeholder: "location_lng",
                            value: this.state.form.location_lng,
                            onChange: this.handleChange
                            }
                        ]}
                        />
                        <Button
                            bsStyle="info"
                            pullRight fill type="submit"
                            onClick={this.onFormSubmit}
                            className="save_button"
                        >
                        {this.state.btnName}
                        </Button>
                    </form>
                    }
                />
                </Col>
                <Location_list 
                    location_datas={this.props.location_datas}
                    onDelete={this.onDelete}
                />
            </Row>
            </Grid>
            </div>
        );
    }
}

export default Location_form;
