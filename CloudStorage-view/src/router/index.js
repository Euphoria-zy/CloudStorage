import {createRouter, createWebHistory} from "vue-router";
import Login from "@/view/Login.vue";
import Index from "@/view/Index.vue";
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
            path: "/viewLog",
            name: "查看日志",
            component: ViewLog
        },
        {
            path: "/index",
            name: "首页",
            component: Index
        }
    ]
});

export default router;