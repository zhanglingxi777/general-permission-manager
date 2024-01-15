<template>
  <div class="app-container">
    <Header/>
    <router-view/>
    <Footer/>
  </div>
</template>
<script>
import Header from "@/components/Portal/header.vue";
import Footer from "@/components/Portal/footer.vue";

export default {
  name: "PortalLayout",
  components: {Footer, Header},
  data() {
    return {}
  },
  methods: {}
}
</script>
<style scoped>
.app-container {
  position: relative;
}

main {
  width: 1080px;
  margin: 20px auto 60px;
  min-height: 260px;
}
</style>
