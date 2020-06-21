import { Card } from "components/Card/Card.jsx";
import { FormInputs } from "components/FormInputs/FormInputs.jsx";
import { UserCard } from "components/UserCard/UserCard.jsx";
import Button from "components/CustomButton/CustomButton.jsx";

import avatar from "assets/img/faces/face-3.jpg";
import React, { Component } from "react";
import {
  Grid,
  Row,
  Col,
  FormGroup,
  ControlLabel,
  FormControl
} from "react-bootstrap";

//auth
import { getProfile } from '../components/Auth/UserFunctions';
import { update } from '../components/Auth/UserFunctions';

class UserProfile extends Component {
  constructor() {
    super()
    this.state = {
        name: '',
        email: '',
        address: '',
        city: '',
        country: '',
        birthday: '',
        about: '',
        errors: {}
    }
    this.onChange = this.onChange.bind(this)
    this.onSubmit = this.onSubmit.bind(this)
}
  componentDidMount() {
    getProfile().then(res => {
        if(res == null) {
          window.location.href = "/";
        } else {
            this.setState({
                name: res.user.name,
                email: res.user.email,
                password: this.state.password,
                pwconfirm: this.state.pwconfirm,
                address: res.user.address,
                city: res.user.city,
                country: res.user.country,
                birthday: res.user.birthday,
                about: res.user.about,
            })
        }
    })
  }

  onChange (e) {
    this.setState({ [e.target.name]: e.target.value })
  }

  onSubmit (e) {
    e.preventDefault();
    
    //validator
    if(this.state.password != this.state.pwconfirm) {
        alert('비밀번호가 맞지않습니다. 다시 확인하세요');
    } else {
        const user = {
            name: this.state.name,
            email: this.state.email,
            password: this.state.password,
            pwconfirm: this.state.pwconfirm,
            address: this.state.address,
            city: this.state.city,
            country: this.state.country,
            about: this.state.about,
            birthday: this.state.birthday,
        }
      console.log('tetstestes', user);

      update(user).then(res => {
        if (res) {
            console.log('업데이트 됐나??', res);
            this.props.history.push(`/user`)
        }
      })
    }
}

  render() {
    let str = this.state.name;
    return (
      <div className="content">
        <Grid fluid>
          <Row>
            <Col md={8}>
              <Card
                title="Edit Profile"
                content={
                  <form onSubmit={this.onSubmit}>

                    <FormInputs
                      ncols={["col-md-5", "col-md-7"]}
                      properties={[
                        {
                          label: "Username",
                          type: "text",
                          bsClass: "form-control",
                          placeholder: "Username",
                          defaultValue: this.state.name,
                          onChange: this.onChange
                        },
                        {
                          label: "Email address",
                          type: "email",
                          bsClass: "form-control",
                          placeholder: "email",
                          defaultValue: this.state.email,
                          onChange: this.onChange
                          
                        }
                      ]}
                    />
                    <FormInputs
                            ncols={["col-md-6", "col-md-6"]}
                            properties={[
                                {
                                label: "Password",
                                type: "password",
                                name: "password",
                                bsClass: "form-control",
                                placeholder: "Password",
                                value: this.state.password,
                                onChange: this.onChange
                                },
                                {
                                label: "Password confirm",
                                type: "password",
                                name: "pwconfirm",
                                bsClass: "form-control",
                                placeholder: "Password confirm",
                                value: this.state.pwconfirm,
                                onChange: this.onChange
                                }
                            ]}
                            />

                    <FormInputs
                      ncols={["col-md-12"]}
                      properties={[
                        {
                          label: "address",
                          type: "text",
                          bsClass: "form-control",
                          placeholder: "Home address",
                          defaultValue: this.state.address,
                          onChange: this.onChange
                        }
                      ]}
                    />
                    <FormInputs
                      ncols={["col-md-6", "col-md-6"]}
                      properties={[
                        {
                          label: "City",
                          type: "text",
                          bsClass: "form-control",
                          placeholder: "City",
                          defaultValue: this.state.city,
                          onChange: this.onChange
                        },
                        {
                          label: "Country",
                          type: "text",
                          bsClass: "form-control",
                          placeholder: "Country",
                          defaultValue: this.state.country,
                          onChange: this.onChange
                        },
                      ]}
                    />
                    <FormInputs
                    ncols={["col-md-8"]}
                    properties={[
                      {
                        label: "Birthday",
                        type: "date",
                        bsClass: "form-control",
                        placeholder: "Country",
                        defaultValue: this.state.birthday,
                        onChange: this.onChange
                      },
                    ]}
                  />
                    <Row>
                      <Col md={12}>
                        <FormGroup controlId="formControlsTextarea">
                          <ControlLabel>About Me</ControlLabel>
                          <FormControl
                            rows="5"
                            componentClass="textarea"
                            bsClass="form-control"
                            placeholder="Here can be your description"
                            value={this.state.about}
                            onChange={this.onChange}
                          />
                        </FormGroup>
                      </Col>
                    </Row>
                    <Button bsStyle="info" pullRight fill type="submit">
                      Update Profile
                    </Button>
                    <div className="clearfix" />
                  </form>
                }
              />
            </Col>
            <Col md={4}>
              <UserCard
                bgImage="https://ununsplash.imgix.net/photo-1431578500526-4d9613015464?fit=crop&fm=jpg&h=300&q=75&w=400"
                avatar={avatar}
                name="Mike Andrew"
                userName="michael24"
                description={
                  <span>
                    "Lamborghini Mercy
                    <br />
                    Your chick she so thirsty
                    <br />
                    I'm in that two seat Lambo"
                  </span>
                }
                socials={
                  <div>
                    <Button simple>
                      <i className="fa fa-facebook-square" />
                    </Button>
                    <Button simple>
                      <i className="fa fa-twitter" />
                    </Button>
                    <Button simple>
                      <i className="fa fa-google-plus-square" />
                    </Button>
                  </div>
                }
              />
            </Col>
          </Row>
        </Grid>
      </div>
    );
  }
}

export default UserProfile;
