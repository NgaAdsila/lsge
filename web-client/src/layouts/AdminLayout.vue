<template>
  <div id="manager-layout" :class="sidebarToggle ? 'sidebar-open' : 'sidebar-close'">
    <div id="manager-layout-left">
      <LeftSideBar />
    </div>
    <div id="manager-layout-right">
      <Header
          :sidebarToggle="sidebarToggle"
          @toggleSideBar="toggleSideBar"
      />
      <b-container fluid>
        <router-view :key="$route.fullPath" />
      </b-container>
      <Footer />
    </div>
  </div>
</template>

<script>
import Header from "@/components/manager/Header";
import LeftSideBar from "@/components/manager/LeftSideBar";
import Footer from "@/components/manager/Footer";
export default {
  name: "AdminLayout",
  components: {Footer, LeftSideBar, Header},
  data() {
    return {
      sidebarToggle: true
    }
  },
  methods: {
    toggleSideBar() {
      this.sidebarToggle = !this.sidebarToggle;
    }
  }
}
</script>

<style lang="scss" scoped>
#manager-layout {
  &.sidebar-open {
    display: flex;
    flex-direction: row;
    #manager-layout-left {
      transition: 0.5s;
      width: 250px;
      height: 100%;
      position: fixed;
      z-index: 1031;
      background-color: rgba(0,0,0,0.6);
      box-shadow: 0 0.25rem 0.25rem rgba(0, 0, 0, 0.25), inset 0 -1px 5px rgba(0, 0, 0, 0.25);
    }
    #manager-layout-right {
      transition: 0.5s;
      margin-left: 250px;
      width: calc(100% - 250px);
    }
  }
  &.sidebar-close {
    #manager-layout-left {
      transition: 0.5s;
      width: 0;
    }
    #manager-layout-right {
      transition: 0.5s;
      margin-left: 0;
      width: 100%;
    }
  }
}
</style>
<style lang="scss">
#manager-layout {
  &.sidebar-open {
    .fixed-top {
      transition: 0.5s;
      left: 250px !important;
    }
  }
  &.sidebar-close {
    .fixed-top {
      transition: 0.5s;
      left: 0;
    }
  }
}
</style>