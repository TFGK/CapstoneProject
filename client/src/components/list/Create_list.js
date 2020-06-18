import React from "react";
import '../css/List.css'
import {getList,addItem} from './ListFunctions';
import { Redirect } from 'react-router-dom';

class Create_list extends React.Component{
  constructor(){
    super()
    this.state={
      id:'',
      title:'',
      content:'',
      editDisabled:false,
      items:[]
    }

    this.onSubmit = this.onSubmit.bind(this)
    this.onChange = this.onChange.bind(this)
  }
  onChange = e =>{
    this.setState({
      [e.target.name]: e.target.value,
      
    })
  }
  componentDidMount(){
    this.getAll()
  }
  getAll = () =>{
    getList().then(data=> {
      this.setState({
        title:'',
        content:'',
        items:[...data]
      },
      () => {
        console.log(this.setState.items)
      })
    })
  }
  onSubmit = e => {
    e.preventDefault()
    addItem(this.state.title, this.state.content).then(()=>{
      this.getAll()
      window.location.href="/list"
    })
    this.setState({
      title:'',
      content:''
    })
  }
  render(){
    return(
      <div>
        <div className="blackline_list"></div>
        <div>
          <form onSubmit={this.onSubmit}>
          <div>
            <label htmlFor="title">Title</label>
            <div>
              <div>
                <input type="text"
                 id="title"
                 name="title" 
                 value={this.state.title || ''}
                 onChange={this.onChange.bind(this)}/>
                 <br/>
                 <textarea
                 id="content"
                 name="content"
                 value={this.state.content || ''}
                 onChange={this.onChange.bind(this)}/>
              </div>
            </div>
          </div>
          {!this.state.editDisabled ? (
            <button type="submit"
            onClick={this.onSubmit.bind(this)}>
              Submit
            </button>
          ) : (
            ''
          )}
          </form>
        </div>
      </div>
    );
  }
}

export default Create_list;