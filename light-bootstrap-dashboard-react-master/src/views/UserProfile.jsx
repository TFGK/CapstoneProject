/*!

=========================================================
* Light Bootstrap Dashboard React - v1.3.0
=========================================================

* Product Page: https://www.creative-tim.com/product/light-bootstrap-dashboard-react
* Copyright 2019 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/light-bootstrap-dashboard-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
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
                      <Col md={20}>
                        <FormGroup>
                          <ControlLabel>제목</ControlLabel>
                          <FormControl
                            id="title"
                            name="title"
                            value={this.state.title || ''}
                            onChange={this.onChange.bind(this)}
                            componentClass="input"
                            bsClass="form-control"
                          />    
                          <ControlLabel>내용</ControlLabel>
                          <FormControl
                            id="content"
                            name="content"
                            value={this.state.content || ''}
                            onChange={this.onChange.bind(this)}
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
                    <Button bsStyle="info" pullRight fill type="submit" onClick={this.onSubmit.bind(this)}>
                      저장
                    </Button>
                    <div className="clearfix" />
                  </form>
                }
              />
            </Col>
            <Col md={4}>
              <UserCard
                bgImage="https://ununsplash.imgix.net/photo-1431578500526-4d9613015464?fit=crop&fm=jpg&h=300&q=75&w=400"
                avatar="https://t1.daumcdn.net/cfile/tistory/243FE450575F82662D"
                name={this.state.name}
                userName={this.state.email}
                description={
                  <span>
                    <br />
                    {this.state.about}
                  </span>
                }
                socials={
                  <div>
                    <Button simple> 
                      Time_line
                    </Button>
                    <Button simple>
                      Articles
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
