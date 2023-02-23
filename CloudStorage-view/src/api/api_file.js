import * as API from "./index"

export default {
    upload: params =>{
        return API.POST("/upload", params)
    }
}