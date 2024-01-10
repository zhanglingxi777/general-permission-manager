<template>
  <div class="app-container">
    <div class="resume-header">
      <div class="resume-header-first"> 简历攻略</div>
      <div class="resume-header-second">求职/ 面试 / 职场</div>
    </div>
    <div class="jingxuan-font">精选推荐</div>
    <div class="resume-main">
      <el-container>
        <el-aside style="width: 500px" class="resume-main-left">
          <el-container class="resume-main-left-main" v-for="index in 3" :key="index">
            <el-aside width="200px" class="resume-main-left-img"></el-aside>
            <el-container>
              <el-header class="resume-main-left-a" style="height: auto"><a target="_blank" href="https://www.zhiyeapp.com/jlgl/VDN0Kh.html">如何做一份优秀的简历</a>
              </el-header>
              <el-main class="resume-main-left-text">
                做出一份简历不难，但是做出一份让HR和面试官满意的优秀简历，不是一件容易的事。
              </el-main>
              <el-footer class="resume-main-left-footer">知页简历 | 2022-02-01</el-footer>
            </el-container>
          </el-container>
        </el-aside>
        <el-container class="resume-main-right">
          <el-header class="resume-main-right-header" style="height: auto">最新简历攻略</el-header>
          <el-main v-for="index in 10" :key="index" class="resume-main-right-text" style="height: auto">
            <el-link href="#"   :underline="false" style="  text-decoration: none;">如何制作高水平简历</el-link>
          </el-main>
        </el-container>
        <el-container class="resume-main-right">
          <el-header class="resume-main-right-header" style="height: auto">最新简历攻略</el-header>
          <el-main v-for="index in 10" :key="index" class="resume-main-right-text" style="height: auto">
            <el-link href="#"    :underline="false" style="  text-decoration: none;">如何制作高水平简历</el-link>
          </el-main>
        </el-container>
      </el-container>
    </div>
  </div>
</template>

<script>
export default {
  name: "resume",
  data() {
    return{

    }
  }
}
</script>

<style scoped>

.resume-header {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.resume-header-first {
  margin-top: 20px;
  text-decoration: none;
  color: #333;
  font-size: 26px;
  line-height: 30px;
  margin-bottom: 8px;
}

.resume-header-second {
  margin-top: 3px;
  margin-bottom: 30px;
  color: #98a1b3;
  font-size: 14px;
  line-height: 22px;
}

.common-layout {
  display: flex;
}

.resume-main-left {
  width: 100px;
  margin: 10px 100px;
}

.jingxuan-font {
  margin: 0 100px;
  color: #333;
  font-size: 18px;
  font-weight: 700;
}

.resume-main-left-img {
  background-image: url("~@/assets/images/recommend_1.jpg");
  background-repeat: no-repeat;
  background-size: cover;
  border-radius: 4px;
  width: 100px;
  height: 130px;
}

.resume-main-left-a > a {
  margin: 0;
  padding-top: 0px;
  padding-bottom: 0px;
  font-size: 16px;
  font-weight: 700;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-decoration: none;
}

.resume-main-left-text {
  padding-top: 10px;
  padding-bottom: 10px;
  color: #555;
  display: -webkit-box;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 12px;
  margin-top: 0;
}

.resume-main-left-main {
  margin: 10px 0;
  display: flex;
}

.resume-main-left-footer {
  font-size: 10px;
  display: flex;
  height: 60px;
  flex-direction: column;
  justify-content: flex-end;
  color: #909399;
}
.resume-main-right{
  margin-left: 0;
  padding: 0;
}

.resume-main-right-text {
  color: #555;
  display: block;
  font-size: 14px;
  overflow: hidden;
  position: relative;
  text-overflow: ellipsis;
  white-space: nowrap;
  padding: 0 0;
  margin: 0 0;
}

.resume-main-right-header {
  color: #333;
  font-size: 16px;
  font-weight: 700;
  padding: 0 0;
  margin: 5px 0;
  display: flex
}
</style>
