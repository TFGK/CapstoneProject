import React, { Component } from "react";


class Location_form extends Component {
    state = {
        form: {location_name: "",
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
            btnName: "Save",
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
        <form className="map_form">
            <div className="map_form_fields">
              <div className="form_location_fields">
                <div className="form_location_name">
                  <h3 className="form_location_title">장소 명</h3>
                  <input
                      type="text"
                      name="location_name"
                      className="form_location_input"
                      placeholder="장소 명을 적어주세요"
                      onChange={this.handleChange}
                      value={this.state.form.location_name}
                  />
                </div>
                
                <div className="form_location_explain">
                  <h3 className="form_location_title">위도</h3>
                  <input 
                      type="text" 
                      name="location_lat"
                      className="form_location_input" 
                      placeholder="위도" 
                      onChange={this.handleChange}
                      value={this.state.form.location_lat}
                  />
                </div>
              </div>
              <div className="form_location_fields">
                <div className="form_location_name">
                  <h3 className="form_location_title">객체타입</h3>
                  <input 
                      type="text" 
                      name="location_type"
                      className="form_location_input" 
                      placeholder="장소 설명을 적어주세요"
                      onChange={this.handleChange}
                      value={this.state.form.location_type}
                  />
                </div>
                <div className="form_location_explain">
                  <h3 className="form_location_title">경도</h3>
                  <input 
                      type="text" 
                      name="location_lng" 
                      className="form_location_input"
                      placeholder="경도" 
                      onChange={this.handleChange}
                      value={this.state.form.location_lng}
                  />
                </div>
              </div>

              <div className="form_button_section">
                  <button
                   className="save_button"
                   onClick={this.onFormSubmit}>
                    {this.state.btnName}
                  </button>
                  {/* <button className="redbutton cancle-button" onClick={this.clearFormFields}>
                      취소
                  </button> */}
              </div>
            </div>
        </form>
        );
    }
}

export default Location_form;
