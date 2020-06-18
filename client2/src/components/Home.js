import React, { useState, useEffect, useRef }  from 'react';
import SimpleSlider from 'infinite-react-carousel';
import TrackVisibility from 'react-on-screen';


import main_img from './images/main_img5.png';
import google from './images/app_google.png';
import hw_img from'./images/hw-img2.jpg';
import default_img from './images/default-image.jpg';
import iPhone from './images/cell_Phone2.png';

import ocr_icon from './images/ocr_icon3.png';
import navigation_icon from './images/navigation_icon2.svg'
import color_icon from './images/color_icon3.svg'
import light_icon from './images/light_icon2.png'
import zoom_icon from './images/zoom_icon.png';

import graph from './images/graph.png';

import app_navi from "./images/app_slide_navi_bgver.png";
import app_ocr from "./images/app_slide_ocr_bgver.png";
import app_color from "./images/app_slide_color_bgver.png";
import app_light from "./images/app_slide_light_bgver.png";
import app_zoom from "./images/app_slide_zoom_bgver.png";


import Slider from "./Slider";

import './css/Home.css';
import './css/animate.css';
// import './modules.css';

const ComponentToTrack = ({ isVisible }) => {
  const style = {
      display: isVisible ? 'initial' : 'none'
  };
  return <p className="main_article">언제 어디서든<br/>
  에이아이(Aeye)를 만나보세요.</p>;
}

class Home extends React.Component{

  render(){
    const settings =  {
      // arrows: false,
      arrowsBlock: false,
      autoplay: true,
      autoplaySpeed: 2000,
      centerMode: true,
      // pauseOnHover:true,
      centerPadding: 230,
      duration: 300,
      // wheel:true,
      shift: 20,
      slidesPerRow: 1,
      // dots: true
    };
    return(
      <div className="article">
        {/* <span className="opacity"></span> */}
        {/* <img src={main_img} className="main_img" /> */}
        <div className="main_section">
          <div className="main_img" >
            <div className="main_article_section">
              <div className="main_article_section_left">
                {/* ㄱㄴㄹ */}
                <img src={iPhone} className="main_cellphone in-left4"></img>
              </div>
              <div className="main_article_section_right">
                <p className="main_article in-left">언제 어디서든<br/>
                에이아이(Aeye)를 만나보세요.</p>
                <p className="sub_article in-left2">Aeye는 '시각장애인'및 '저시력자'등<br/>
                시각의 보조가 필요한 사용자들에게<br/>
                스마트폰 카메라를 통해 인식한 정보를 알려주는 서비스입니다.</p>
                
              <img src={google} className="google_button in-left3"></img>
              </div>
            </div>
          </div>
          
        </div>

        {/* <div class="mainbar">
            <a href=".hw-part" >제품소개</a>
            <a href=".app-part">앱 정보</a>
            <a href=".guide-part" >가이드 영상</a>
        </div>
        */}

        <div className="article_hw">
          <div className="hw-img">
            <img src={default_img} width="100%" height="600px" ></img>
          </div>
          <div className="hw-article">
            <h2>제품소개</h2><br/>
            영진전문대학<br/>
            WDJ 1조의 캡스톤프로젝트<br/>
            Aeye입니다 <br/>
          </div>
        </div>

        <div className="article_app">
          <div className="app_spec">
            <h1 className="app_spec_title wow bounceInUp">
              앱 소개
            </h1>
            <div className="app_spec_article">
              <div className="app_article_left">
                <img src={graph} className="app_spec_graph" />
                <p className="app_spec_note">장애인 스마트폰 보유율은 70%를 넘어섭니다.</p>
              </div>
              <div className="app_article_right">
                <p>스마트폰도 사용 못 할 것이라는 편견이 있지만,</p>
                <h3>실제 시각장애인에게 스마트폰은 필수입니다.</h3>
                <span className="app_article_bar" >
                　　　　　　　　　　　　　　　　　　　　　　　　
                </span>
                <p>많은 시각장애인 분들의 눈과 손이 되어</p>
                <p>희망을 볼 수 있길 기원하며 Aeye앱을 개발하였습니다.</p>
                <p>시각장애인이 보통 사람과 같이 불편함 없이 생활할 수</p>
                <p>있는 날을 꿈꾸며 개발해나가겠습니다.</p>
              </div>
            </div>
          </div>
        </div>

        <div className="introduce_app_section">
          <div><h1 className="introduce_app_title">앱 기능</h1></div>
          <div className="introduce_app">          
            <div className="introduce_app_article">
              <img className="app_article_img" src={navigation_icon} alt=""/>
              <p className="role_name">길 찾기</p>
            </div>
            <div className="introduce_app_article">
              <img className="app_article_img" src={ocr_icon} alt="" />
              <p className="role_name">문자인식</p>              
            </div>
            <div className="introduce_app_article">
              <img className="app_article_img" src={color_icon} alt="" />
              <p className="role_name">색상 인식</p>              
            </div>
            <div className="introduce_app_article">
              <img className="app_article_img" src={light_icon} alt="" />
              <p className="role_name">빛 밝기</p>
            </div>
            <div className="introduce_app_article">
              <img className="app_article_img" src={zoom_icon} alt="" />
              <p className="role_name">돋보기 기능</p>
            </div>
          </div>
        </div>

        {/* <div className="app_slide">
          <Slider></Slider>
        </div> */}

        {/* <Slider></Slider> */}
        {/* <InfiSlider></InfiSlider> */}

        <div className="app_slide2">
        {/* <span>앱 기능</span> */}
        <SimpleSlider { ...settings }>
          <div className="app_slide_img">
            <img className="img2" src={app_navi} alt="" />
          </div>
          <div className="app_slide_img">
            <img className="img2" src={app_ocr} alt="" />
          </div>
          <div className="app_slide_img">
            <img className="img2" src={app_color} alt="" />
          </div>
          <div className="app_slide_img">
           <img className="img2" src={app_light} alt="" />
          </div>
          <div className="app_slide_img">
            <img className="img2" src={app_zoom} alt="" />
          </div>
        </SimpleSlider>
        </div>
        
        <div className="article_video">
          <div className="videobox">
          <h1 className="video-h1">가이드 영상</h1>
          <div className="video-content">
          <iframe width="100%" height="400" src="https://www.youtube.com/embed/PQEfodbDODg" frameBorder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowFullScreen></iframe>
          </div>
          </div>
        </div>
        {/* <div class="button-1">
          <div class="eff-1"></div>
          <p> ADD</p>
        </div> */}
      </div>
    )
  };
}


export default Home;