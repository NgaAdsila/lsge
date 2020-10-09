<template>
  <div class="manager-layout-header">
    <b-navbar type="dark" variant="dark" fixed="top"
              class="header-container">
      <b-navbar-nav>
        <b-nav-item @click="toggleSidebar">
          <b-icon v-show="sidebarToggle" icon="arrow-left-circle" animation="cylon"></b-icon>
          <b-icon v-show="!sidebarToggle" icon="arrow-right-circle" animation="cylon"></b-icon>
        </b-nav-item>
        <b-nav-item to="/">
          {{ $t('common.label.home') }}
        </b-nav-item>
      </b-navbar-nav>
      <b-navbar-nav class="ml-auto">
        <b-nav-text>
          <Avatar :avatar="$store.getters.avatar"
                  :color="$store.getters.color"
                  :name="$store.getters.name"
                  default-color="#28a745"
          /> <em>{{ $store.getters.name }} </em>
        </b-nav-text>
      </b-navbar-nav>
    </b-navbar>
  </div>
</template>

<script>
import Avatar from "@/components/common/Avatar";
export default {
  name: "ManagerHeader",
  components: {Avatar},
  props: ['sidebarToggle'],
  methods: {
    toggleSidebar() {
      this.$emit('toggleSideBar');
    }
  }
}
</script>

<style scoped>

</style>