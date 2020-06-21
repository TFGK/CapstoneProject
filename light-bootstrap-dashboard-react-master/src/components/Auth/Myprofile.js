import React, { Component } from 'react'
import { getProfile } from './UserFunctions'
import '../css/Myprofile.css';

// Timeline component
import Timeline from './Timeline';
import Register from './Register';

import Timeline2 from './Timeline2';
import Timeline3 from './Timeline3';

class Myprofile extends Component {
    constructor() {
        super()
        this.state = {
            name: '',
            email: '',
            year: '',
        }
    }
    componentDidMount() {
        getProfile().then(res => {
            if(res == null) {
                alert('로그인 해주세요');
                window.location.href = "/";
            } else {
                this.setState({
                    name: res.user.name,
                    email: res.user.email,
                    year: res.user.year,
                })                
            }
        })
    }

    Editprofile = () => {
        window.open('/regester');
    }

    openTab = (tabName) => e => {
        // Declare all variables
        var i, tabcontent, tablinks;
        e.preventDefault();
      
        // Get all elements with class="tabcontent" and hide them
        tabcontent = document.getElementsByClassName("tabcontent");
        for (i = 0; i < tabcontent.length; i++) {
          tabcontent[i].style.display = "none";
        }
      
        // Get all elements with class="tablinks" and remove the class "active"
        tablinks = document.getElementsByClassName("nav-link");
        for (i = 0; i < tablinks.length; i++) {
          tablinks[i].className = tablinks[i].className.replace(" active", "");
        }
      
        // Show the current tab, and add an "active" class to the button that opened the tab
        document.getElementById(tabName).style.display = "block";
        e.currentTarget.className += " active";
      }

    render() {
        const { e, openTab } = this.props;
        return (
            <div class="container emp-profile">
                <form method="post">
                    <div class="row">
                        <div class="col-md-4">
                            <div class="profile-img">
                                <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS52y5aInsxSm31CvHOFHWujqUx_wWTS9iM6s7BAm21oEN_RiGoog" alt=""/>
                                <div class="file btn btn-lg btn-primary">
                                    Change Photo
                                    <input type="file" name="file"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="profile-head">
                                        <br />
                                        <h1>
                                            {/* my name */}
                                            {this.state.name}
                                        </h1>
                                        <h4>
                                            My Profile page
                                        </h4>
                                        <br />

                                {/* tab list => 마이프로필의 타임라인과 게시글의 탭 */}
                                <ul class="nav nav-tabs" id="myTab" role="tablist">
                                    {/* <li class="nav-item">
                                        <button class="tablinks" onClick={this.openTab('Timeline')}>Timeline</button>
                                    </li>
                                    <li class="nav-item">
                                        <button class="tablinks" onClick={this.openTab('Articles')}>Articles</button>
                                    </li> */}
                                    <li class="nav-item">
                                        <a class="nav-link" id="home-tab" data-toggle="tab" role="tab" aria-controls="home" aria-selected="false"
                                        onClick={this.openTab('Timeline')}>Timeline</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="profile-tab" data-toggle="tab" role="tab" aria-controls="profile" aria-selected="false"
                                        onClick={this.openTab('Articles')}>MyArticles</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="col-md-2">
                            <input type="submit" onClick={this.Editprofile} class="profile-edit-btn" name="btnAddMore" value="Edit Profile"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4">
                        <div class="tab-content profile-tab" id="myTabContent">
                                <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <label>Name</label>
                                                </div>
                                                <div class="col-md-6">
                                                    <p>{this.state.name}</p>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <label>Email</label>
                                                </div>
                                                <div class="col-md-6">
                                                    <p>{this.state.email}</p>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <label>Birthday</label>
                                                </div>
                                                <div class="col-md-6">
                                                    <p>{this.state.year}</p>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <label>Phone</label>
                                                </div>
                                                <div class="col-md-6">
                                                    <p>123 456 7890</p>
                                                </div>
                                            </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-8">
                            <div id="Timeline" class="tabcontent">
                                <hr />
                                <h6>Road map log v1</h6>
                                <Timeline2 />
                                <hr />
                                {/* <h6>Road map log v2</h6>
                                <Timeline3 /> */}
                            </div>

                            <div id="Articles" class="tabcontent">
                                <h3>Articles</h3>
                                <p>Paris is the capital of France.</p>
                                </div>
                            </div>
                        </div>
                </form>           
            </div>
        )
    }
}

export default Myprofile
