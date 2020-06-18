import React from 'react';
import axios from 'axios';
import { Redirect } from 'react-router-dom';

export const getList = () => {
  return axios
  .get('/api/tasks',{
    headers: {'Content-Type': 'application/json'}
  })
  .then(res=>{
    return res.data
  })
}

export const addItem = (title, content) => {
  return axios
  .post('/api/task',
    {
      title:title,
      content:content
    },
    {
      headers: {'Content-Type': 'application/json'}
    }
  )
  .then(res => {
    console.log(res);
  })
}

export const deleteItem = id =>{
  axios.delete(`api/task/${id}`,{
    headers: {'Content-Type': 'application/json'}
  })
  .then(res => {
    console.log(res)
  })
  .catch(err => {
    console.log(err)
  })
}

export const updateItem = (title,id) =>{
  return axios
    .put(`api/task/${id}`,{
      title: title
    },{
      headers: { 'Content-Type': 'appplication/json' }
    })
    .then(res => {
      console.log(res)
    })
    .catch(err => {
      console.log(err)
    })

}