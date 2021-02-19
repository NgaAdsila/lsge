<template>
    <div>
        {{ postId }}
    </div>
</template>

<script>
    export default {
        name: "index",
        computed: {
            postId: function () {
                return this.$route.params.id
            }
        }
    }
</script>

<style scoped>

</style>