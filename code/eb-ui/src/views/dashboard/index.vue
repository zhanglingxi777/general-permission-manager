<template>
  <div class="dashboard-container">
    <el-row>
      <el-col :span="24">
        <component :is="currentRole"/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import adminDashboard from './admin'
import editorDashboard from './editor'

export default {
  name: 'Dashboard',
  components: {adminDashboard, editorDashboard},
  data() {
    return {
      currentRole: 'adminDashboard',
      role: null,
      isSeeker: false,
      isHr: false,
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
