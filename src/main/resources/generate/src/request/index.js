import axios from "axios";

let baseUrlPrefix = ""
let baseUrl = "http://192.168.0.222:10086/back";

export const request = axios.create({
    timeout: 3600000,
    headers: {
        "Access-Control-Allow-Origin": "*",
        "Content-type": "application/json; charset=utf-8"
    },
    baseURL: baseUrl,
    withCredentials: false
})

// 请求拦截器
request.interceptors.request.use(function (config) {
    // console.log('发请求之前', config)
    return config
}, function (error) {
    // console.log('请求错误', error)
    return Promise.reject(error)
})

// 响应拦截器
request.interceptors.response.use(function (response) {
    // console.log('得到的响应数据', response)
    return response
}, function (error) {
    // console.log('响应错误', error)
    return Promise.reject(error)
})


// get请求
export function get(url, data = {}) {
    return new Promise((resolve, reject) => {
        request.get(baseUrlPrefix + url, {
            params: data
        })
            .then((response) => {
                resolve(response)
            })
            .catch((err) => {
                reject(err)
            })
    })
}

// post请求
export function post(url, data = {}) {
    return new Promise((resolve, reject) => {
        request.post(baseUrlPrefix + url, data).then(
            (response) => {
                resolve(response.data)
            },
            (err) => {
                reject(err)
            }
        )
    })
}

// PUT请求
export function put(url, data = {}) {
    return new Promise((resolve, reject) => {
        request.put(baseUrlPrefix + url, {
            params: data
        })
            .then((response) => {
                resolve(response)
            })
            .catch((err) => {
                reject(err)
            })
    })
}

// DELETE请求
export function del(url, data = {}) {
    return new Promise((resolve, reject) => {
        request.delete(baseUrlPrefix + url, data).then(
            (response) => {
                resolve(response.data)
            },
            (err) => {
                reject(err)
            }
        )
    })
}

export function postForm(url, data = {}, responseType = {}) {
    return new Promise((resolve, reject) => {
        request.post(baseUrlPrefix + url, data, responseType).then(
            (response) => {
                resolve(response.data)
            },
            (err) => {
                reject(err)
            }
        )
    })
}


export default request
