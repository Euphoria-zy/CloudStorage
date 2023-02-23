import {createRouter, createWebHistory} from "vue-router";
import Login from "@/view/Login.vue";
import Index from "@/view/Index.vue";
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
            component: Index
        }
    ]
});

export default router;