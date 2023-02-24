import * as API from "./index"

export default {

    getAll: params =>{
        return API.POST("/index/file-list", params)
    },
    uploadFile: params =>{
        return API.POST("/uploadFile", params)
    },
    uploadFolder: params =>{
        return API.POST("/uploadFolder", params)
    },
    addFolder: params =>{
        return API.POST("/addFolder", params)
    },
    downloadFile: params =>{
        return API.POST("/downloadFile", params)
    },
    renameFile: params =>{
        return API.POST("/renameFile", params)
    },
    renameFolder: params =>{
        return API.POST("/renameFolder", params)
    },
    deleteFile: params =>{
        return API.POST("/deleteFile", params)
    },
    deleteFolder: params =>{
        return API.POST("/deleteFolder", params)
    },
    enterFolder: params =>{
        return API.POST("/getFolder", params)
    },
    copyFileOrFolder: params =>{
        return API.POST("/copyFileOrFolder", params)
    },
    moveFileOrFolder: params =>{
        return API.POST("/moveFileOrFolder", params)
    },
    getFileByType: params =>{
        return API.POST("/index/getFileByType", params)
    }
}