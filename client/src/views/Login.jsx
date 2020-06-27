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


import { login } from '../components/Auth/UserFunctions';

class Login extends Component {
    constructor() {
        super()
        this.state = {
            email: '',
            password: '',
            pwconfirm: '',
            errors: {}
        }
        this.onChange = this.onChange.bind(this)
        this.onSubmit = this.onSubmit.bind(this)
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value })
    }

    onSubmit (e) {
        e.preventDefault();
            const user = {
                email: this.state.email,
                password: this.state.password
            }

            console.log('tetstestes', user);

            login(user).then(res => {
                if (res) {
                    console.log('토큰인가?', res);
                    this.props.history.push(`/admin/main`)
                }
        })
    }

    render() {
        return (
            <div className="content">
                <Grid fluid>
                <Row>
                    <Col md={3}/>
                    <Col md={6}>
                    <Card 
                        title="Login Page"
                        content={
                        <form onSubmit={this.onSubmit}>
                            <FormInputs
                            ncols={["col-md-10"]}
                            properties={[
                                {
                                label: "Email address",
                                type: "email",
                                name: "email",
                                bsClass: "form-control",
                                placeholder: "email",
                                value: this.state.email,
                                onChange: this.onChange
                                }
                            ]}
                            />
                            
                            <FormInputs
                            ncols={["col-md-10"]}
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
                                // {
                                // label: "Password confirm",
                                // type: "password",
                                // name: "pwconfirm",
                                // bsClass: "form-control",
                                // placeholder: "Password confirm",
                                // value: this.state.pwconfirm,
                                // onChange: this.onChange
                                // }
                            ]}
                            />

                            <Button bsStyle="info" pullRight fill type="submit">
                            Login
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


export default Login
