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
import { Grid, Row, Col, Table } from "react-bootstrap";
import Card from "components/Card/Card.jsx";
import Button from "components/CustomButton/CustomButton.jsx";
import {getList } from 'components/Board/boardFucn.js';
import { Link } from "react-router-dom";
import "../assets/css/dong.css";

var thArray = ['No', "Title"]
var freeArray = []
var sugesstionArray = []
var modifyArray = []
class TableList extends Component {
  constructor(){
    super()
    this.state={
      id:'',
      title:'',
      content:'',
      category:'',
      editDisabled:false,
      items:[],
    }
    // this.getAll = this.getAll.bind(this)
    this.getAll()
  }

  componentWillMount(){
    freeArray = []
    sugesstionArray = []
    modifyArray = []
  }
  componentDidMount(){
    
  }
  componentDidUpdate(){
    
  }
getAll = () =>{
  getList().then(data=> {
    this.setState({
      id:'',
      title:'',
      content:'',
      category:'',
      items:[...data]
    },
    () => {
      this.state.items.map((item, index)=>{
          if(item.category === "modify"){
            modifyArray.push([item.id, item.title])
            console.log(modifyArray)
          }else if(item.category === "free"){
            freeArray.push([item.id, item.title])
            console.log(freeArray)
          }else if(item.category === "sugesstion"){
            sugesstionArray.push([item.id, item.title])
            console.log(sugesstionArray)
          }
          else{
            console.log("Test")
          }
      })
    })
  })
}

  render() {
    return (
      <div className="content">
        <Link to="/admin/create">
          <Button bsStyle="info" fill className="table_btn">
            글쓰기
          </Button>
        </Link>
        <Grid fluid>
          <Row>
            <Col md={12}>
              <Card
                title="자유게시판"
                // category="Here is a subtitle for this table"
                ctTableFullWidth
                ctTableResponsive
                content={
                  <Table striped hover>
                    <thead>
                      <tr>
                        {thArray.map((prop, key) => {
                          return <th key={key}>{prop}</th>;
                        })}
                      </tr>
                    </thead>
                    <tbody>
                      {freeArray.map((prop, key) => {
                        return (
                          <tr key={key}>
                            {prop.map((prop, key) => {
                              return <td key={key}>{prop}</td>;
                            })}
                          </tr>
                        );
                      })}
                    </tbody>
                  </Table>
                }
              />
            </Col>

            <Col md={12}>
              <Card
                plain
                title="Q & A"
                // category="Here is a subtitle for this table"
                ctTableFullWidth
                ctTableResponsive
                content={
                  <Table hover>
                    <thead>
                      <tr>
                        {thArray.map((prop, key) => {
                          return <th key={key}>{prop}</th>;
                        })}
                      </tr>
                    </thead>
                    <tbody>
                      {sugesstionArray.map((prop, key) => {
                        return (
                          <tr key={key}>
                            {prop.map((prop, key) => {
                              return <td key={key}>{prop}</td>;
                            })}
                          </tr>
                        );
                      })}
                    </tbody>
                  </Table>
                }
              />
            </Col>

            <Col md={12}>
              <Card
                title="데이터 수정 요청"
                // category="Here is a subtitle for this table"
                ctTableFullWidth
                ctTableResponsive
                content={
                  <Table striped hover>
                    <thead>
                      <tr>
                        {thArray.map((prop, key) => {
                          return <th key={key}>{prop}</th>;
                        })}
                      </tr>
                    </thead>
                    <tbody>
                      {modifyArray.map((prop, key) => {
                        return (
                          <tr key={key}>
                            {prop.map((prop, key) => {
                              return <td key={key}>{prop}</td>;
                            })}
                          </tr>
                        );
                      })}
                    </tbody>
                  </Table>
                }
              />
            </Col>
          </Row>
        </Grid>
      </div>
    );
  }
}

export default TableList;
