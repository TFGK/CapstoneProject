import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter as Router, Link, Route} from 'react-router-dom';
import './components/css/index.css';


// 실질적으로 사용할 컴포넌트 불러오기
import Home from './components/Home';
import Footer from './components/Footer';
import Buy from './components/Buy';
import Location from './components/location/Location';
import Navigation from "./components/Navigation";
import * as serviceWorker from './serviceWorker';

// auth
import Register from './components/Auth/Register';
import Login from './components/Auth/Login';
import Myprofile from './components/Auth/Myprofile';

//articles
import List from './components/list/List';
import Create from './components/list/Create_list';

export default class Index extends Component {
    render() {
        return (
            <div>
                <Router>
                    <div className="route">
                    <Navigation />
                        <div className="container">
                            <Route path="/" exact component={Home} />
                            <Route path="/location" exact component={Location}/>
                            <Route path="/buy" exact component={Buy}/>
                            <Route path="/list" exact component={List}/>
                            <Route path="/create" component={Create}/>

                            {/* auth */}
                            <Route path="/login" exact component={Login}/>
                            <Route path="/register" exact component={Register}/>
                            <Route path="/Myprofile" exact component={Myprofile}/>
                        </div>
                    </div>
                </Router> 
            <Footer />
            </div>
        )
    }
}
ReactDOM.render(<Index />, document.querySelector('#root'));
serviceWorker.unregister();



