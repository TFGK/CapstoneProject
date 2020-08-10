import { Card } from "components/Card/Card.jsx";
import { FormInputs } from "components/FormInputs/FormInputs.jsx";
import Button from "components/CustomButton/CustomButton.jsx";

import React, { Component } from "react";
import {
  Grid,
  Row,
  Col,
  FormGroup,
  ControlLabel,
  FormControl
} from "react-bootstrap";

import { register } from '../components/Auth/UserFunctions';

class Register extends Component {
    constructor() {
        super()
        this.state = {
            name: '',
            email: '',
            password: '',
            address: '',
            city: '',
            country: '',
            about: '',
            birthday: '',
            errors: {}
        }
        this.onChange = this.onChange.bind(this)
        this.onSubmit = this.onSubmit.bind(this)
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
            const newUser = {
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
            console.log('tetstestes', newUser);

            register(newUser).then(res => {
                this.props.history.push(`/login`)
            })
        }
    }

    render () {
        return (
            <div className="content">
                <Grid fluid>
                <Row>
                    <Col md={3}/>
                    <Col md={6}>
                    <Card
                        title="新規取得"
                        content={
                        <form onSubmit={this.onSubmit}>
                            <FormInputs
                            ncols={["col-md-5", "col-md-7"]}
                            properties={[
                                {
                                label: "名前",
                                type: "text",
                                name: "name",
                                bsClass: "form-control",
                                placeholder: "名前",
                                value: this.state.name,
                                onChange: this.onChange
                                },
                                {
                                label: "メールアドレス",
                                type: "email",
                                name: "email",
                                bsClass: "form-control",
                                placeholder: "メールアドレス",
                                value: this.state.email,
                                onChange: this.onChange
                                }
                            ]}
                            />
                            
                            <FormInputs
                            ncols={["col-md-6", "col-md-6"]}
                            properties={[
                                {
                                label: "パスワード",
                                type: "password",
                                name: "password",
                                bsClass: "form-control",
                                placeholder: "パスワード",
                                value: this.state.password,
                                onChange: this.onChange
                                },
                                {
                                label: "パスワード確認",
                                type: "password",
                                name: "pwconfirm",
                                bsClass: "form-control",
                                placeholder: "パスワード確認",
                                value: this.state.pwconfirm,
                                onChange: this.onChange
                                }
                            ]}
                            />

                            <FormInputs
                            ncols={["col-md-12"]}
                            properties={[
                                {
                                label: "住所",
                                type: "text",
                                name: "address",
                                bsClass: "form-control",
                                placeholder: "住所",
                                value: this.state.address,
                                onChange: this.onChange
                                }
                            ]}
                            
                            />
                            <FormInputs
                            ncols={["col-md-6", "col-md-6"]}
                            properties={[
                                {
                                label: "都市名",
                                type: "text",
                                name: "city",
                                bsClass: "form-control",
                                placeholder: "都市名",
                                value: this.state.city,
                                onChange: this.onChange
                                },
                                {
                                label: "国家名",
                                type: "text",
                                bsClass: "form-control",
                                placeholder: "国家名",
                                name: "country",
                                value: this.state.country,
                                onChange: this.onChange
                                },
                            ]}
                            />
                            <FormInputs
                            ncols={["col-md-8"]}
                            properties={[
                                {
                                    label: "生年月日",
                                    type: "date",
                                    bsClass: "form-control",
                                    name: "birthday",
                                    placeholder: "生年月日",
                                    value: this.state.birthday,
                                    onChange: this.onChange
                                },
                            ]}
                            />
                            <Row>
                            <Col md={12}>
                                <FormGroup controlId="formControlsTextarea">
                                <ControlLabel>自己紹介</ControlLabel>
                                <FormControl
                                    rows="5"
                                    componentClass="textarea"
                                    name="about"
                                    bsClass="form-control"
                                    placeholder="自己紹介"
                                    value={this.state.about}
                                    onChange={this.onChange}
                                />
                                </FormGroup>
                            </Col>
                            </Row>
                            <Button bsStyle="info" pullRight fill type="submit">
                            確認
                            </Button>
                            <div className="clearfix" />
                        </form>
                        }
                    />
                    </Col>
                </Row>
                </Grid>
            </div>
        )
    }
}

export default Register
