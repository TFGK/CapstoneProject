import React, { Component } from "react";
import '../assets/css/main.css';
import '../assets/css/main-animate.css';

import google from '../assets/img/app_google_jp.png';
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
              <p className="main_article in-left">
              いつでも、どこでも<br/>
              Aeyeを
              ご利用ください。
              </p>
              <p className="sub_article in-left2">
              Aeyeは視覚障害者、「低視力者」など<br/>
              視覚の補助が必要なユーザーに<br/>
              スマートフォンのカメラを通じて認識した<br/>
              情報を教えるサービスです。
              </p>
            <a href="https://play.google.com/store" target="_blank">
              <img src={google} className="google_button in-left2" alt="googleBt" />
            </a> 
            </div>
          </div>
        </div>
        
      </div>
    )
  }
}

export default Main;