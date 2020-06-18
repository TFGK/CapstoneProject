import axios from 'axios'
import { defaultsDeep } from 'lodash'

export const register = newUser => {
    return axios
        .post('api/register', newUser, {
            headers: { 'Content-Type': 'application/json' }
        })
        .then(response => {
            console.log(response)
        })
        .catch(err => {
            console.log(err)
        })
}

export const login = user => {
    return axios
        .post('/api/login',
            {
                email: user.email,
                password: user.password
            },
            {
                headers: { 'Content-Type': 'application/json' }
            }
        )
        .then(response => {
            localStorage.setItem('usertoken', response.data.token)
            return response.data.token
        })
        .catch(err => {
            console.log(err)
            alert('비밀번호를 확인하세요');
        })
}

export const getProfile = () => {
    return axios
        .get('/api/profile', {
            headers: { Authorization: `Bearer ${localStorage.usertoken}` }
        })
        .then(response => {
            console.log("test",response)
            return response.data
        })
        .catch(err => {
            console.log(err)
        })
}
