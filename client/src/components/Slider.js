import React, { useState, useEffect, useRef } from "react";
import styled from "styled-components";

import img1 from "./routes_img/img1.jpg";
import img2 from "./routes_img/img2.jpg";
import img3 from "./routes_img/img3.jpg";
import img4 from "./routes_img/img4.jpg";
import img5 from "./routes_img/img5.jpg";
import img6 from "./routes_img/img6.jpg";
import img7 from "./routes_img/img7.jpg";

import './css/Home.css'

const Container = styled.div`
  width: 80%;
  overflow: hidden; 
  // margin-left:100px;
`;
// 선을 넘어간 이미지들은 보이지 않도록 처리합니다.
const Button = styled.button`
  all: unset;
  border: 1px solid coral;
  padding: 0.5em 2em;
  color: coral;
  border-radius: 10px;
  &:hover {
    transition: all 0.3s ease-in-out;
    background-color: coral;
    color: #fff;
  }
`;
const SliderContainer = styled.div`
  width: 100%;
  display: flex; //이미지들을 가로로 나열합니다.
  background:pink;
`;
const TOTAL_SLIDES = 6;

export default function Slider() {
  const [currentSlide, setCurrentSlide] = useState(0);
  const slideRef = useRef(null);

  const nextSlide = () => {
    if (currentSlide >= TOTAL_SLIDES) { // 더 이상 넘어갈 슬라이드가 없으면 슬라이드를 초기화합니다.
      setCurrentSlide(0);
    } else {
      setCurrentSlide(currentSlide + 1);
    }
  };
  const prevSlide = () => {
    if (currentSlide === 0) {
      setCurrentSlide(TOTAL_SLIDES);
    } else {
      setCurrentSlide(currentSlide - 1);
    }
  };
  useEffect(() => {
    slideRef.current.style.transition = "all 0.5s ease-in-out";
    slideRef.current.style.transform = `translateX(-${currentSlide}00%)`; // 백틱을 사용하여 슬라이드로 이동하는 애니메이션을 만듭니다.
  }, [currentSlide]);
  return (
    <Container>
      {/* {currentSlide} */}
      <SliderContainer ref={slideRef}>
        {/* <Slide img={img1} />
        <Slide img={img2} />
        <Slide img={img3} /> */}
         <img src={img1} className="img1" ></img>
         <img src={img2} className="img1" ></img>
         <img src={img3} className="img1" ></img>
         <img src={img4} className="img1" ></img>
         <img src={img5} className="img1" ></img>
         <img src={img6} className="img1" ></img>
         <img src={img7} className="img1" ></img>
      </SliderContainer>
      <Button onClick={prevSlide}>Previous Slide</Button>
      <Button onClick={nextSlide}>Next Slide</Button>
    </Container>
  );
}