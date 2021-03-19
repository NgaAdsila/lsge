<template>
  <div id="manager-layout" :class="(sidebarToggle ? 'sidebar-open' : 'sidebar-close') + (mobileToggle ? ' sidebar-mobile' : '')">
    <div id="manager-layout-left"
         class="bg-dark"
         @mouseover="sidebarToggle = true"
         @mouseout="sidebarToggle = isToggle">
      <LeftSideBar :sidebarToggle="sidebarToggle" />
    </div>
    <div id="manager-layout-right">
      <Header
          :mobileToggle="mobileToggle"
          :sidebarToggle="sidebarToggle"
          @toggleSideBar="toggleSideBar"
      />
      <b-container fluid>
        <router-view :key="$route.fullPath"
                     :onlineUsers="onlineUsers" />
      </b-container>
      <Footer />
    </div>
    <div v-if="mobileToggle && isToggle" class="manager-layout-mobile-toggle" @click="toggleSideBar"></div>
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
      mobileToggle: false,
      onlineUsers: [],
      echoConnect: null
    }
  },
  mounted() {
    this.registerChannel()
    this.handlePageChangeSize()
    window.addEventListener('resize', this.handlePageChangeSize)
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
    },
    handlePageChangeSize() {
      if (window.innerWidth <= 768) {
        this.mobileToggle = true
        this.sidebarToggle = false
        this.isToggle = false
      } else {
        if (this.mobileToggle) {
          this.sidebarToggle = true
          this.isToggle = true
        }
        this.mobileToggle = false
      }
    }
  },
  beforeDestroy() {
    if (this.echoConnect) {
      this.echoConnect.leave(ECHO_CHANNEL.CHANNEL_MAIN);
      this.echoConnect = null;
    }
    window.removeEventListener('resize', this.handlePageChangeSize)
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
    /*background-color: rgba(0,0,0,0.7);*/
    box-shadow: 0 0.25rem 0.25rem rgba(0, 0, 0, 0.25), inset 0 -1px 5px rgba(0, 0, 0, 0.25);
  }
  &.sidebar-open:not(.sidebar-mobile) {
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
  &.sidebar-open.sidebar-mobile {
    display: flex;
    flex-direction: row;
    #manager-layout-left {
      transition: 0.5s;
      width: 250px;
    }
  }
  &.sidebar-close:not(.sidebar-mobile) {
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
  &.sidebar-close.sidebar-mobile {
    #manager-layout-left {
      transition: 0.5s;
      margin-left: -250px;
    }
  }
  .manager-layout-mobile-toggle {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 1030;
  }
}
</style>
<style lang="scss">
#manager-layout {
  &.sidebar-open:not(.sidebar-mobile) {
    .fixed-top {
      transition: 0.5s;
      left: 250px !important;
    }
  }
  &.sidebar-close:not(.sidebar-mobile) {
    .fixed-top {
      transition: 0.5s;
      left: 3.5rem;
    }
  }
  &.sidebar-close.sidebar-mobile {
    .fixed-top {
      transition: 0.5s;
      left: 0;
    }
  }
}
</style>