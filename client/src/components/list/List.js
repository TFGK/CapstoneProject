import React, {Component} from 'react';
import {getList, addItem, deleteItem, updateItem} from './ListFunctions';
// import { Divider } from './node_modules/@material-ui/core';


import { Link } from "react-router-dom";
import Table from '@material-ui/core/Table';
import TableHead from '@material-ui/core/TableHead';
import TableBody from '@material-ui/core/TableBody';
import TableRow from '@material-ui/core/TableRow';
import TableCell from '@material-ui/core/TableCell';

import '../css/List.css'

class List extends Component{
  constructor(){
    super()
    this.state={
      id:'',
      title:'',
      editDisabled:false,
      items:[]
    }

    this.onSubmit = this.onSubmit.bind(this)
    this.onChange = this.onChange.bind(this)
  }

  componentDidMount(){
    this.getAll()
  }

  onChange = e =>{
    this.setState({
      [e.target.name]: e.target.value
    })
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
    addItem(this.state.title).then(()=>{
      this.getAll();
      
    })
    this.setState({
      title:''
    })
  }

  onUpdate = e =>{
    e.preventDefault()
    updateItem(this.state.title, this.state.id).then(()=>{
      this.getAll()
    })
    this.setState({
      editDisabled: ''
    })
  }

  onEdit = (itemid, e) => {
    e.preventDefault()

    var data = [...this.state.items]
    data.forEach((item,index)=>{
      if(item.id === itemid){
        this.setState({
          id: item.id,
          title: item.title,
          editDisabled: true
        })
      }
    })
  }

  onDelete = (val, e) => {
    e.preventDefault()
    deleteItem(val)
    this.getAll()
  }

  render() {
    return(
      <div className="list_body">
        <div className="blackline_list"></div>
        <div className="bulletin_top_img">
          <div className="bulletin_img_title">게시판</div>
        </div>
        <div>
          <Link to="create">
            글쓰기
          </Link>
        </div>
        {/* <form onSubmit={this.onSubmit}>
          <div>
            <label htmlFor="title">Title</label>
            <div>
              <div>
                <input type="text"
                 id="title"
                 name="title" 
                 value={this.state.title || ''}
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
          {this.state.editDisabled ? (
            <button type="submit"
            onClick={this.onUpdate.bind(this)}>
              Update
            </button>
          ) : (
            ''
          )}
        </form> */}
        {/* <table>
          <tbody>
            {this.state.items.map((item, index) => {
              return(
              <tr key={index}>
                <td>{item.id}</td>
                <td>{item.title}</td>
                <td>
                  <button
                  href=""
                  disabled={this.state.editDisabled}
                  onClick={this.onEdit.bind(
                    this,
                    item.id
                   )}>
                   Edit
                  </button>
                  <button
                  href=""
                  disabled={this.state.editDisabled}
                  onClick={this.onDelete.bind(
                    this,
                    item.id
                   )}>
                   Delete
                  </button>
                </td>
              </tr>
              )
            })}
          </tbody>
        </table> */}
        <Table>
          <TableHead>
            <TableCell>NO.</TableCell>
            <TableCell>제목</TableCell>
            <TableCell>내용</TableCell>
            <TableCell>버튼</TableCell>
          </TableHead>
          {this.state.items.map((item, index) => {
            return(
              <TableBody>
                <TableRow>
                  <TableCell>{item.id}</TableCell>
                  <TableCell>{item.title}</TableCell>
                  <TableCell>{item.content}</TableCell>
                  <TableCell>
                  <button
                    href=""
                    disabled={this.state.editDisabled}
                    onClick={this.onEdit.bind(
                      this,
                      item.id
                      )}>
                      Edit
                    </button>
                    <button
                    href=""
                    disabled={this.state.editDisabled}
                    onClick={this.onDelete.bind(
                      this,
                      item.id
                      )}>
                      Delete
                    </button>
                  </TableCell>
                </TableRow>
              </TableBody>
            )
          })}
        </Table>
      </div>
    )
  }
}

export default List;