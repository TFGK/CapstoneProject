import React, { Component } from "react";
import { NavItem, Nav, NavDropdown, MenuItem } from "react-bootstrap";
import { withRouter } from 'react-router-dom' ;
class AdminNavbarLinks extends Component {
  // auth
  logOut(e) {

    e.preventDefault()
    localStorage.removeItem('usertoken')
    this.props.history.push(`/`)
  }
  render() {
    // Auth div tag
    const loginRegLink = (
      <Nav pullRight>
          <NavItem eventKey={1} href="/admin/login">
            ログイン
          </NavItem>

        <NavItem eventKey={1.1} href="/admin/register">
          新規習得
        </NavItem>
      </Nav>
    )

    const userLink = (
      //로그인 했을 때 user 정보가 뜸
      <Nav pullRight>
        <NavItem eventKey={1.1} href="" onClick={this.logOut.bind(this)}>
          ログアウト
        </NavItem>
      </Nav>
    )

    return (
      <div>
        <Nav>
          <NavItem eventKey={1} href="#">
            <i className="fa fa-dashboard" />
            <p className="hidden-lg hidden-md">Dashboard</p>
          </NavItem>
        </Nav>
        {localStorage.usertoken ? userLink : loginRegLink}
      </div>
    );
  }
}

export default withRouter(AdminNavbarLinks);