<template>
    <RegisterComponent
            @register="register"/>
</template>

<script>
    import RegisterComponent from "../components/RegisterComponent";
    export default {
        name: "Register",
        components: {RegisterComponent},
        methods: {
            register(data = {}) {
                console.log('DATA: ', data);
            }
        }
    }
</script>

<style scoped>

</style>