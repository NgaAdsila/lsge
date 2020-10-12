<template>
  <div class="manager-layout-left-sidebar">
    <div class="manager-layout-sidebar-header has-link">
      <b-img v-show="sidebarToggle"
             src="@/assets/logo.png" height="50"
             @click="redirectTo('/manager')"
             :alt="$t('common.label.slogan')" />
      <span v-show="sidebarToggle">{{ $t('manager.label.home') }}</span>
      <span v-show="!sidebarToggle">{{ $t('manager.label.short_name') }}</span>
    </div>
    <div class="manager-layout-sidebar-menu">
      <b-list-group>
        <b-list-group-item v-for="(menu, index) in menus"
                           :key="index"
                           :active="$route.name === menu.routeName"
                           :to="{name: menu.routeName}">
          <b-icon :icon="menu.icon"></b-icon> <span v-show="sidebarToggle" class="menu-item">{{ menu.name }}</span>
        </b-list-group-item>
      </b-list-group>
    </div>
  </div>
</template>

<script>
export default {
  name: "ManagerLeftSideBar",
  props: ['sidebarToggle'],
  data() {
    return {
      menus: [
        {
          name: this.$t('manager.label.dashboard'),
          icon: 'house-fill',
          routeName: 'ManagerHome'
        }, {
          name: this.$t('manager.label.user_list'),
          icon: 'people',
          routeName: 'ManagerUser'
        }, {
          name: this.$t('manager.label.file'),
          icon: 'folder',
          routeName: 'ManagerFile'
        }
      ]
    }
  },
  methods: {
    redirectTo(path) {
      if (this.$route.path === path) {
        return
      }
      this.$router.push(path)
    }
  }
}
</script>

<style lang="scss" scoped>
.manager-layout-left-sidebar {
  color: #FFFFFF;
  .manager-layout-sidebar-header {
    height: 4.5rem;
    line-height: 4.5rem;
    text-align: center;
    font-weight: 400;
    border-bottom: 1px solid gray;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    img {
      margin-left: -1.75rem;
    }
  }
  .manager-layout-sidebar-menu {
    .list-group {
      border-radius: unset;
      .list-group-item {
        background-color: transparent;
        white-space: nowrap;
        overflow: hidden;
        .menu-item {
          text-overflow: ellipsis;
        }
        color: #FFFFFF;
        &.active {
          background-color: #007bff;
        }
        &:hover {
          cursor: pointer;
          background-color: rgba(0,0,0,0.4);
          text-decoration: unset;
        }
      }
    }
  }
}
</style>