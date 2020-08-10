import { Card } from "components/Card/Card.jsx";
import { FormInputs } from "components/FormInputs/FormInputs.jsx";
import { UserCard } from "components/UserCard/UserCard.jsx";
import Button from "components/CustomButton/CustomButton.jsx";
import React, { Component } from "react";
import "../assets/css/timeline.css";
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
import { getTimeline } from '../components/Auth/UserFunctions';
import { update } from '../components/Auth/UserFunctions';
import './loader.js'

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
        errors: {},
        timeline1 : '',
        timeline2 : '',
        timeline3 : '',
        timeline4 : '',
        timeline5 : ''
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
    .then( () => {
      const data = {
          email: this.state.email
      }
      getTimeline(data).then(res => {
        if(res == null ) {
          window.location.href = "/";
        }else{
          this.setState({
          
          })
        }
        // console.log('getTimeline userprofiles 63 ', res);
      });
    });
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
            birthday: this.state.birthday,
            city: this.state.city,
            country: this.state.country,
            about: this.state.about,
            address: this.state.address,
            // pwconfirm: this.state.pwconfirm,      
        }
        const id = this.state.email;
      console.log('tetstestes', user);

      update(user, id).then(res => {
        if (res) {
            console.log('업데이트 됐나??', res);
            window.location.href="/admin/userprofile"
        }
      })
    }
  }
  more(e){
    e.preventDefault();
  }

  render() {
    return (
      <div className="content">
        <Grid fluid>
          <Row>
            <Col md={8}>
              <Card
                title="プロフィール"
                content={
                  <form onSubmit={this.onSubmit}>

                    <FormInputs
                      ncols={["col-md-5", "col-md-7"]}
                      properties={[
                        {
                          label: "名前",
                          name: "name",
                          type: "text",
                          bsClass: "form-control",
                          placeholder: "名前",
                          defaultValue: this.state.name,
                          onChange: this.onChange
                        },
                        {
                          label: "メールアドレス",
                          name : "email",
                          type: "email",
                          bsClass: "form-control",
                          placeholder: "メールアドレス",
                          defaultValue: this.state.email,
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
                          name: "address",
                          type: "text",
                          bsClass: "form-control",
                          placeholder: "住所",
                          defaultValue: this.state.address,
                          onChange: this.onChange
                        }
                      ]}
                    />
                    <FormInputs
                      ncols={["col-md-6", "col-md-6"]}
                      properties={[
                        {
                          label: "都市名",
                          name: "city",
                          type: "text",
                          bsClass: "form-control",
                          placeholder: "都市名",
                          defaultValue: this.state.city,
                          onChange: this.onChange
                        },
                        {
                          label: "国家名",
                          name: "country",
                          type: "text",
                          bsClass: "form-control",
                          placeholder: "国家名",
                          defaultValue: this.state.country,
                          onChange: this.onChange
                        },
                      ]}
                    />
                    <FormInputs
                    ncols={["col-md-6"]}
                    properties={[
                      {
                        label: "生年月日",
                        name: "birthday",
                        type: "date",
                        bsClass: "form-control",
                        placeholder: "生年月日",
                        defaultValue: this.state.birthday,
                        onChange: this.onChange
                      },
                    ]}
                  />
                    <Row>
                      <Col md={12}>
                        <FormGroup>
                          <ControlLabel>自己紹介</ControlLabel>
                          <FormControl
                            id="content"
                            name="content"
                            value={this.state.content || ''}
                            onChange={this.onChange.bind(this)}
                            rows="5"
                            componentClass="textarea"
                            bsClass="form-control"
                            placeholder="自己紹介"
                            value={this.state.about}
                            onChange={this.onChange}
                          />
                        </FormGroup>
                      </Col>
                    </Row>
                    <Button bsStyle="info" pullRight fill type="submit" onClick={this.onSubmit.bind(this)}>
                      保存する
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
                    タイムライン	
                    </Button>
                  </div>
                }
              />
            </Col>
            <Col md={12}>
              <div class="page-header">
                <h2>タイムライン</h2>
                <br />
              </div>
              <div>
              <ul class="timeline timeline-horizontal">
                <li class="timeline-item">
                  <div class="timeline-badge primary"><i class="glyphicon glyphicon-check"></i></div>
                  <div class="timeline-panel">
                    <div class="timeline-heading">
                      <h4 class="timeline-title">청담면옥 복현점</h4>
                    </div>
                    <div class="timeline-body">
                    </div>
                  </div>
                </li>
                <li class="timeline-item">
                  <div class="timeline-badge success"><i class="glyphicon glyphicon-check"></i></div>
                  <div class="timeline-panel">
                    <div class="timeline-heading">
                      <h4 class="timeline-title">한올미용실</h4>
                    </div>
                    <div class="timeline-body">
                    </div>
                  </div>
                </li>
                <li class="timeline-item">
                  <div class="timeline-badge info"><i class="glyphicon glyphicon-check"></i></div>
                  <div class="timeline-panel">
                    <div class="timeline-heading">
                      <h4 class="timeline-title">훼미리마트 복현슬기점</h4>
                    </div>
                    <div class="timeline-body">
                    </div>
                  </div>
                </li>
                <li class="timeline-item">
                  <div class="timeline-badge danger"><i class="glyphicon glyphicon-check"></i></div>
                  <div class="timeline-panel">
                    <div class="timeline-heading">
                      <h4 class="timeline-title">교동면옥 복현점</h4>
                    </div>
                    <div class="timeline-body">
                    </div>
                  </div>
                </li>
              </ul>
              </div>
            </Col>
          </Row>
        </Grid>
      </div>
    );
  }
}


export default UserProfile;
