<template>
    <b-form>
        <p class="h4 text-center mb-4">{{ $t('register.label.title') }}</p>

        <b-form-group label-cols="2" label-cols-lg="2" label-size="sm"
                      :label="$t('common.label.username')"
                      label-for="input-username">
            <b-form-input id="input-username" size="sm"
                          v-model="username"
                          class="grey-text"></b-form-input>
        </b-form-group>
        <b-form-group label-cols="2" label-cols-lg="2" label-size="sm"
                      :label="$t('common.label.password')"
                      label-for="input-password">
            <b-form-input type="password" id="input-password" size="sm"
                          v-model="password"
                          class="grey-text"></b-form-input>
        </b-form-group>
        <b-form-group label-cols="2" label-cols-lg="2" label-size="sm"
                      :label="$t('register.label.re_password')"
                      label-for="input-re_password">
            <b-form-input type="password" id="input-re_password" size="sm"
                          v-model="re_password"
                          class="grey-text"></b-form-input>
        </b-form-group>
        <b-form-group label-cols="2" label-cols-lg="2" label-size="sm"
                      :label="$t('common.label.email')"
                      label-for="input-email">
            <b-form-input type="email" id="input-email" size="sm"
                          v-model="email"
                          class="grey-text"></b-form-input>
        </b-form-group>
        <div class="text-center mt-4">
            <b-button @click="register">{{ $t('register.label.submit') }}</b-button>
        </div>
        <div class="text-center mt-4">
            {{ $t('register.label.login_link') }}
            <router-link to="/login">{{ $t('common.label.login') }}</router-link>!
        </div>
    </b-form>
</template>

<script>
    export default {
        name: "RegisterComponent",
        data() {
            return {
                username: '',
                password: '',
                re_password: '',
                email: ''
            }
        },
        methods: {
            register() {
                this.$emit('register', {
                    username: this.username,
                    password: this.password,
                    email: this.email
                })
            }
        }
    }
</script>

<style scoped>
</style>