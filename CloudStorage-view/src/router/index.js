import {createRouter, createWebHistory} from "vue-router";
import Login from "@/view/Login.vue";
import Index from "@/view/Index.vue";
import Document from "@/view/Document.vue";
import Video from "@/view/Video.vue";
import Image from "@/view/Image.vue";
import Music from "@/view/Music.vue";
import Other from "@/view/Other.vue";
import ViewLog from "@/view/ViewLog.vue";

let router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: "/login",
            name: "登录",
            component: Login
        },
        {
            path: "/",
            name: "首页",
            component: Index,
            redirect: "/index"
        },
        {
            path: "/index",
            name: "全部文件",
            component: Index
        },
        {
            path: "/document",
            name: "文档",
            component: Document
        },
        {
            path: "/video",
            name: "视频",
            component: Video
        },
        {
            path: "/music",
            name: "音乐",
            component: Music
        },
        {
            path: "/image",
            name: "图片",
            component: Image
        },
        {
            path: "/other",
            name: "其他",
            component: Other
        },
        {
            path: "/viewLog",
            name: "查看日志",
            component: ViewLog
        },
    ]
});
//路由守卫：to:要去的路由,from: 当前路由,next(): 放行
router.beforeEach((to, from, next) => {
    if (to.path.startsWith('/login')) {
        window.localStorage.removeItem('access-token')
        next()
    } else if (to.path.startsWith('/index')) {
        let user = window.localStorage.getItem('access-token');
        if (!user) {
            next({path: '/login'})
        } 
        next()
    } else if (to.path.startsWith('/viewLog')) {
        let user = window.localStorage.getItem('access-token');
        if (!user) {
            next({path: '/login'})
        } 
        next()
    } else {
        next()
    }
});
export default router;