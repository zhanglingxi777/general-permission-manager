<template>
  <div class="app-container">
    <profile/>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import Profile from "@/views/profile/index.vue";

export default {
  name: 'Dashboard',
  components: {Profile},
  data() {
    return {
      currentRole: 'adminDashboard',
      role: null,
      showList: []
    }
  },
  computed: {
    ...mapGetters([
      'roles'
    ])
  },
  created() {
    if (!this.roles.includes('admin')) {
      this.currentRole = 'editorDashboard'
    }
  }
}
</script>
<style lang="scss" scoped>

</style>
