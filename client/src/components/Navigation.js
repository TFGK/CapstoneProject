import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import Aeye_white from './images/Aeye_white.png';
import "./css/Navigation.css";
import "./css/Buy.css";

class Home extends Component {

  // auth
  logOut(e) {
    e.preventDefault()
    localStorage.removeItem('usertoken')
    this.props.history.push(`/`)
  }

  render() {

    const loginRegLink = (
      <ul className="aeye_navbar_ul">
        <li className="aeye_navbar_li" key="8">
          <Link to="/login">로그인</Link>
        </li>

        <li className="aeye_navbar_li" key="9">
          <Link to="/register">회원가입</Link>
        </li>
      </ul>
    )

    const userLink = (
      //로그인 했을 때 user 정보가 뜸
      <ul className="aeye_navbar_ul">
        <li className="aeye_navbar_li" key="5">
          <Link to="/Myprofile">내 정보</Link>
        </li>

        <li className="aeye_navbar_li">
          <a href="" onClick={this.logOut.bind(this)} className="nav-link">
            로그아웃
          </a>
        </li>
      </ul>
    )

    return (
      <div>
      <span className="navbar_opac"></span>
      <div className="aeye_navbar">
        <Link to="/"><img src={Aeye_white} className="App-logo" alt="logo" /></Link>
          <ul className="aeye_navbar_ul">
              <li className="aeye_navbar_li" key="1">
              <Link to="building">서비스 소개</Link>
              </li>
              <li className="aeye_navbar_li" key="2">
              <Link to="location">지도정보</Link>
              </li>
              <li className="aeye_navbar_li" key="3">
              <Link to="buy">구매하기</Link> 
              </li>
              <li className="aeye_navbar_li" key="4">
              <Link to="list">게시판</Link>
              </li>
            </ul>
            {localStorage.usertoken ? userLink : loginRegLink}
        </div>
      </div>
    )
  }
}
export default withRouter(Home);