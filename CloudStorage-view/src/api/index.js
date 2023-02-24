import axios from "axios";
let baseurl = "http://localhost:8080";

export default {
    baseurl
}

export const POST = (url, params) =>{
    return axios.post(`${baseurl}${url}`, params).then(res => res.data);
}

export const GET = (url, params) =>{
    return axios.get(`${baseurl}${url}`, {params: params}).then(res => res.data);
}