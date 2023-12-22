<script setup>

</script>

<template>
  <div style="width: 100vw; height: 100vh; overflow: hidden; display: flex">
    <div style="flex: 1; background-color: black">
      <el-image style="width: 100%; height: 100%" fit="cover" src="https://www.absolutviajes.com/wp-content/uploads/2020/05/playas-asturias.jpg"></el-image>
    </div>

    <div class="welcome-title">
      <div style="font-size: 30px; font-weight: bold">欢迎来到宿舍管理系统</div>
      <div style="margin-top: 10px">简洁、高效、全新的校园楼舍管理系统</div>
    </div>

    <div class="right-card">
      <router-view/>
    </div>
  </div>
</template>

<style scoped>
.right-card {
  width: 400px;
  z-index: 1;
  background-color: white;
}
.welcome-title {
  position: absolute;
  bottom: 30px;
  left: 60px;
  color: white;
  text-shadow: 0 0 10px black;
}
</style>