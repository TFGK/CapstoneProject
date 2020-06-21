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
import React, { Component } from "react";
import {
  Grid,
  Row,
  Col,
  FormGroup,
  ControlLabel,
  FormControl
} from "react-bootstrap";

import { Card } from "components/Card/Card.jsx";
import { FormInputs } from "components/FormInputs/FormInputs.jsx";
import { UserCard } from "components/UserCard/UserCard.jsx";
import Button from "components/CustomButton/CustomButton.jsx";

import avatar from "assets/img/faces/face-3.jpg";

import {getList,addItem} from 'components/Board/boardFucn.js';

class UserProfile extends Component {
  constructor(){
    super()
    this.state={
      id:'',
      title:'',
      content:'',
      category:'',
      editDisabled:false,
      items:[]
    }

    this.onSubmit = this.onSubmit.bind(this)
    this.onChange = this.onChange.bind(this)
  }

  onChange = e =>{
    this.setState({
      [e.target.name]: e.target.value,
    })
  }

  componentDidMount(){

  }
  onSubmit = e => {
    e.preventDefault()
    addItem(this.state.title, this.state.content, this.state.category).then(()=>{
      window.location.href="/admin/table"
    })
      this.setState({
        title:'',
        content:'',
        category:'free',
      })
  }

  render() {
    return (
      <div className="content">
        <Grid fluid>
          <Row>
            <Col md={8}>
              <Card
                title="Edit Profile"
                content={
                  <form onSubmit={this.onSubmit} id="myArticle">
                    <FormInputs
                      ncols={0}
                      properties={0}
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
                            defaultValue="Lamborghini Mercy, Your chick she so thirsty, I'm in that two seat Lambo."
                          />    
                          <ControlLabel>카테고리</ControlLabel>
                          <FormControl
                            id="category"
                            name="category"
                            componentClass="select"
                            onChange={this.onChange.bind(this)}
                            bsClass="form-control"
                            value={this.state.category || ''}
                          >
                            <option value="">-- 선택 --</option>
                            <option value="free">자유게시판</option>
                            <option value="sugesstion">Q & A</option>
                            <option value="modify">데이터 수정 요청</option>
                          </FormControl>
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
          </Row>
        </Grid>
      </div>
    );
  }
}

export default UserProfile;
