<template>
    <RegisterComponent
            @register="onRegister"/>
</template>

<script>
    import RegisterComponent from "../components/RegisterComponent";
    import { register } from '../services/user_service.js';
    export default {
        name: "Register",
        components: {RegisterComponent},
        methods: {
            onRegister(data = {}) {
                const user = register(data);
                console.log(user);
            }
        }
    }
</script>

<style scoped>

</style>