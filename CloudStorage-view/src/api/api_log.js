import * as API from "./index"
export default {
    getAllLog: () =>{
        return API.POST("/index/log-list")
    },
    deleteLog: params =>{
        return API.POST("/deleteLog", params)
    },
    clearLog: () =>{
        return API.POST("/clearLog")
    }
}