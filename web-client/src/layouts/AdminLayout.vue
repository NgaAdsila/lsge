<template>
  <div id="manager-layout" :class="sidebarToggle ? 'sidebar-open' : 'sidebar-close'">
    <div id="manager-layout-left" @mouseover="sidebarToggle = true" @mouseout="sidebarToggle = isToggle">
      <LeftSideBar :sidebarToggle="sidebarToggle" />
    </div>
    <div id="manager-layout-right">
      <Header
          :sidebarToggle="sidebarToggle"
          @toggleSideBar="toggleSideBar"
      />
      <b-container fluid>
        <router-view :key="$route.fullPath"
                     :onlineUsers="onlineUsers" />
      </b-container>
      <Footer />
    </div>
  </div>
</template>

<script>
import Header from "@/components/manager/Header";
import LeftSideBar from "@/components/manager/LeftSideBar";
import Footer from "@/components/manager/Footer";
import {ECHO_CHANNEL} from "@/services/constants";
import {initEcho} from "@/helper/EchoClientHelper";
export default {
  name: "AdminLayout",
  components: {Footer, LeftSideBar, Header},
  data() {
    return {
      sidebarToggle: true,
      isToggle: true,
      onlineUsers: [],
      echoConnect: null
    }
  },
  mounted() {
    this.registerChannel()
  },
  methods: {
    toggleSideBar() {
      this.sidebarToggle = !this.sidebarToggle;
      this.isToggle = !this.isToggle;
    },
    async registerChannel() {
      if (!this.echoConnect) {
        this.echoConnect = await initEcho();
      }
      this.echoConnect.join(ECHO_CHANNEL.CHANNEL_MAIN)
        .here(users => {
          this.onlineUsers = users
        })
        .joining(user => {
          this.onlineUsers.push(user)
        })
        .leaving(user => {
          const index = this.onlineUsers.findIndex(u => u.id === user.id)
          if (index > -1) {
            this.onlineUsers.splice(index, 1);
          }
        })
    }
  },
  beforeDestroy() {
    if (this.echoConnect) {
      this.echoConnect.leave(ECHO_CHANNEL.CHANNEL_MAIN);
      this.echoConnect = null;
    }
  }
}
</script>

<style lang="scss" scoped>
#manager-layout {
  #manager-layout-left {
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    z-index: 1031;
    background-color: rgba(0,0,0,0.7);
    box-shadow: 0 0.25rem 0.25rem rgba(0, 0, 0, 0.25), inset 0 -1px 5px rgba(0, 0, 0, 0.25);
  }
  &.sidebar-open {
    display: flex;
    flex-direction: row;
    #manager-layout-left {
      transition: 0.5s;
      width: 250px;
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
      width: 3.5rem;
    }
    #manager-layout-right {
      transition: 0.5s;
      margin-left: 3.5rem;
      width: calc(100% - 3.5rem);
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
      left: 3.5rem;
    }
  }
}
</style>