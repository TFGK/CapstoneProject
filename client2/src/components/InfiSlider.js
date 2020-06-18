import React, { Component } from 'react';
import Slider from 'infinite-react-carousel';

import img1 from "./images/img1.jpg";
import img2 from "./images/img2.jpg";
import img3 from "./images/img3.jpg";
import img4 from "./images/img4.jpg";
import img5 from "./images/img5.jpg";

class InfiSlider extends React.Component {
  render() {
    const settings =  {
      // arrows: false,
      // arrowsBlock: false,
      // autoplay: true,
      // centerMode: true,
      // centerPadding: 100
    };
    return (
      <div>
        <span>CustomSlider</span>
        <Slider { ...settings }>
          <div>
            <h3 stlye="backgorund:blue;">1</h3>
          </div>
          <div>
            <h3>2</h3>
          </div>
          <div>
            <h3>3</h3>
          </div>
          <div>
            <h3>4</h3>
          </div>
          <div>
            <h3>5</h3>
          </div>
        </Slider>
      </div>
    );
  }
}

export default InfiSlider;