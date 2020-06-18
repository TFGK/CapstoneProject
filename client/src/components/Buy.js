import React from "react";
import Aeye_white from './images/Aeye_white.png';
import Aeye from './images/Aeye.png';
import './css/Buy.css';

class Buy extends React.Component{
  constructor(props) {
    super(props);
    this.state = {
      value: '',
      name: `Aeye Type-1`,
      price: `0,000,000`
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }
  handleChange(event) {
    console.log(event.value);
    if(event.target.value === ''){
      alert('상품을 선택해 주세용!');
    }
    this.setState({value: event.target.value});
  }
  handleSubmit(event) {
    console.log(this.state.value);
    alert('The product you ordered: ' + this.state.value);
    event.preventDefault();
  }

  render(){
    return(
      <div className="area_buy">
        <div className="blackline" />
        <div className="article_buy">
          <div className="img_buy">
            <div id="slide">
              <input type="radio" name="pos" id="pos1" defaultChecked>
              </input>
              <input type="radio" name="pos" id="pos2">
              </input>
              <input type="radio" name="pos" id="pos3">
              </input>
              <input type="radio" name="pos" id="pos4">
              </input>
              <ul>
                <li></li>
                <li></li>
                <li></li>
                <li></li>
              </ul>
              <p className="pos">
                <label htmlFor="pos1"></label>
                <label htmlFor="pos2"></label>
                <label htmlFor="pos3"></label>
                <label htmlFor="pos4"></label>
               </p>
            </div>
          </div>
          
          <div className="option_buy">
            <img src={Aeye} className="aeye_logo_buy"></img>
            <div className="option_buy_title">
              <span className="main-title">{this.state.name}</span>
              <span className="series">AEYE Series</span>
            </div>
            <hr/>

            <div className="price_area">
              <span className="price">{this.state.price} 원</span>
            </div>
            <hr/>

            <div className="country_area">
              <div className="first_section">
                <span className="country">제조사 / 제조국</span>
                <span className="country">배송정보</span>
              </div>
              <div className="second_section">
                <span className="country_article">WDJGroup.1 / Republic of Korea</span>
                <span className="country_article">택배배송 / 5만원 이상 구매시 무료배송</span>
              </div>
            </div>
            <hr/>
            
            <div className="option_area">
              <div className="option_fir_section">
                <span className="option_title">
                 옵션
                </span>
              </div>
              <div className="option_sec_section">
                <span className="option_dropdown">
                  <form onSubmit={this.handleSubmit}>
                    <select id="browsers2" value={this.state.value} onChange={this.handleChange}>
                      <option value="" defaultValue>상품을 선택해 주세요.</option>
                      <option value="Type1">Aeye Type-1</option>
                    </select>
                    <input className="order_button" type="submit" value="구매하기" />
                  </form>
                </span>
              </div>
            </div>

          </div>         
        </div>
      </div>
    )
  };
}

export default Buy;