<template>
    <header>
        <div class="top-flex-container">
            <div class="header-left">
                <img src="../assets/product_logo.jpg" alt="产品logo" height="30">
                <p>极速云盘</p>
            </div>
            <!-- 将网盘 logo 和用户信息隔开 -->
            <div class="header-center"></div>
            <div class="header-right">
                <el-avatar></el-avatar>
                <p>{{ userName }}</p>
                <el-button link>退出登录</el-button>
            </div>
        </div>
    </header>
</template>

<script>
export default {
    name: "TopBar",
    data() {
        return {
            userName: "zhou"
        }
    }
}
</script>

<style scoped>
header {
    height: 55px;
    box-shadow: 0 3px 10px 0 rgb(0 0 0 / 6%);
    padding: 0 24px;
}

.top-flex-container,
.header-left,
.header-right {
    display: flex;
    align-items: center;
}

.header-left {
    justify-content: space-between;
    min-width: 110px;
}

.header-right {
    justify-content: space-between;
    min-width: 156px;
}

.header-center {
    flex: 1 100px;
    min-width: 100px;
}
</style>