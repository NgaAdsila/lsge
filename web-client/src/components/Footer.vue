<template>
    <div class="fluid-container footer">
        <p class="text-center">Welcome to my page!</p>
    </div>
</template>

<script>
    export default {
        name: "Footer"
    }
</script>

<style scoped>
    .fluid-container.footer{
        background: darkgray;
    }
    .fluid-container.footer > *:last-child {
        margin-bottom: 0;
        color: #fff;
    }
</style>