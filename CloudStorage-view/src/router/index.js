import {createRouter, createWebHistory} from "vue-router";
import Login from "@/view/Login.vue";
import Index from "@/view/Index.vue";
import ViewLog from "@/view/ViewLog.vue";
import DocumentTable from "@/components/DocumentTable.vue";
import VideoTable from "@/components/VideoTable.vue";
import ImageTable from "@/components/ImageTable.vue";
import MusicTable from "@/components/MusicTable.vue";
import OtherTable from "@/components/OtherTable.vue";
import FileList from "@/view/Files.vue";
let router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: "/login",
            name: "登录",
            component: Login
        },
        {
            path: "/index",
            name: "首页",
            component: Index,
            redirect: "/index/files",
            children: 
            [
                {
                    path: "/index/files",
                    component: FileList
                },
                {
                    path: "/index/document",
                    component: DocumentTable
                },
                {
                    path: "/index/video",
                    component: VideoTable
                },
                {
                    path: "/index/music",
                    component: MusicTable
                },
                {
                    path: "/index/other",
                    component: OtherTable
                },
                {
                    path: "/index/image",
                    component: ImageTable
                },
                
            ]
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