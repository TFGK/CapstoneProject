import axios from 'axios'

export const register = newUser => {
    return axios
        .post('/api/register', newUser, {
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
        })
}

export const update = (user, id) => {
    return axios
        .put(`/api/update/${id}`,
            {
              name: user.name,
              email: user.email,
              password: user.password,
              address: user.address,
              city: user.city,
              country: user.country,
              about: user.about,
              birthday: user.birthday,
                
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
        })
}

export const getProfile = () => {
    return axios
        .get('/api/profile', {
            headers: { Authorization: `Bearer ${localStorage.usertoken}` }
        })
        .then(response => {
            console.log("getProfiles",response)
            return response.data
        })
        .catch(err => {
            console.log(err)
        })
}

export const getTimeline = (data) => {
    return axios
        .get('/api/timelines', {
            headers: { Authorization: `Bearer ${localStorage.usertoken}` }
        })
        .then(response => {
            console.log("getProfiles",JSON.stringify(response))
            return response.data
        })
        .catch(err => {
            console.log(err)
        })
}
