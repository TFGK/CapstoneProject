import React, { Component } from "react";
import '../assets/css/main.css';
import '../assets/css/main-animate.css';

import google from '../assets/img/app_google.png';
import iPhone from '../assets/img/cell_Phone2.png';

class Main extends Component{
  render(){
    return(
      <div className="main_section">
        <div className="main_img" >
          <div className="main_article_section">
            <div className="main_article_section_left">
              {/* ㄱㄴㄹ */}
              <img src={iPhone} className="main_cellphone in-left3" alt="iPhone"></img>
            </div>
            <div className="main_article_section_right">
              <p className="main_article in-left">언제 어디서든<br/>
              에이아이(Aeye)를 만나보세요.</p>
              <p className="sub_article in-left2">Aeye는 '시각장애인'및 '저시력자'등<br/>
              시각의 보조가 필요한 사용자들에게<br/>
              스마트폰 카메라를 통해 인식한 정보를 알려주는 서비스입니다.</p>
              
            <img src={google} className="google_button in-left2" alt="googleBt"></img>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

export default Main;