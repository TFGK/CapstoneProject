import React from "react";
import Aeye from './images/Aeye.png';
import Aeye_white from './images/Aeye_white.png';
import './css/Footer.css';

function Footer(){
  return(
    <div className="Footer">

      <div className="first-section">
      <p>Aeye</p>
      (주)WDJ 1조 AEYE 만드는 사람들<br/>
      사업자등록번호는 없습니다.  전화번호 010-3377-5092<br/>
      Copyright © WDJ Group1, Ltd. All rights reserved.
      </div>

      <div className="second-section">
      AEYE소개<br/>
      지도정보<br/>
      건물정보<br/>
      구매하기<br/>
      </div>

      <div className="third-section">
      소개<br/>
      개인정보보호정책<br/>
      이용약관
      </div>

      <div className="logo-section"> 
        {/* <div className="footer-logo" src={Aeye}></div> */}
       <img className="footer-logo" src={Aeye_white} alt="aeye" />
      </div>
    </div>
  );
}

export default Footer;