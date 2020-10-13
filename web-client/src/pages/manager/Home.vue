<template>
  <div class="manager-home-page">
    <ManagerDashboardStateComponent
            :onlineUsers="onlineUsers" />
    <hr/>
    <ManagerDashboardListComponent
            :onlineUsers="onlineUsers" />
  </div>
</template>

<script>

import ManagerDashboardStateComponent from "../../components/manager/home/DashboardStateComponent";
import ManagerDashboardListComponent from "../../components/manager/home/DashboardListComponent";
export default {
  name: "ManagerHome",
  components: {ManagerDashboardListComponent, ManagerDashboardStateComponent},
  props: ['onlineUsers'],
  data() {
    return {
      isLoading: true
    }
  },
  mounted() {
  },
  beforeDestroy() {
  }
}
</script>

<style scoped>

</style>