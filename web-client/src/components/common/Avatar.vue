<template>
    <span>
        <b-avatar v-if="avatar" :src="avatar" :size="size"></b-avatar>
        <b-avatar v-else class="text-uppercase"
                  :style="'background-color: ' + (color || defaultColor)"
                  :size="size"
                  :text="name ? name.charAt(0) : ''"></b-avatar>
    </span>
</template>

<script>
export default {
    name: "Avatar",
    props: ['avatar', 'color', 'name', 'defaultColor', 'size']
}
</script>

<style scoped>

</style>