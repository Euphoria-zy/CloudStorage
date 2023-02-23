import * as API from "./index"

export default {
    login: params =>{
        return API.POST("/user/login", params)
    },
    register: params =>{
        return API.POST("/user/register", params)
    },
    getRegisterCode: params =>{
        return API.POST("/user/getRegisterCode", params)
    },
    forgetPwd: params =>{
        return API.POST("/user/forgetPwd", params)
    },
    getForgetPwdCode: params =>{
        return API.POST("/user/getChangePwdCode", params)
    }
}