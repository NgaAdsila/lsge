<template>
    <b-form>
        <p class="h4 text-center mb-4">{{ $t('login.label.title') }}</p>

        <b-form-group label-cols="2" label-cols-lg="2" label-size="sm"
                      :label="$t('login.label.username')"
                      label-for="input-username">
            <b-form-input id="input-username" size="sm"
                          v-model="username"
                          class="grey-text"></b-form-input>
        </b-form-group>
        <b-form-group label-cols="2" label-cols-lg="2" label-size="sm"
                      :label="$t('login.label.password')"
                      label-for="input-password">
            <b-form-input type="password" id="input-password" size="sm"
                          v-model="password"
                          class="grey-text"></b-form-input>
        </b-form-group>
        <b-form-group label-cols="2" label-cols-lg="2" label-size="sm"
                      :label="$t('login.label.re_password')"
                      label-for="input-re_password">
            <b-form-input type="password" id="input-re_password" size="sm"
                          v-model="re_password"
                          class="grey-text"></b-form-input>
        </b-form-group>
        <b-form-group label-cols="2" label-cols-lg="2" label-size="sm"
                      :label="$t('login.label.email')"
                      label-for="input-email">
            <b-form-input type="email" id="input-email" size="sm"
                          v-model="email"
                          class="grey-text"></b-form-input>
        </b-form-group>
        <div class="text-center mt-4">
            <b-button @click="submitLogin">{{ $t('login.label.submit') }}</b-button>
        </div>
    </b-form>
</template>

<script>
    export default {
        name: "LoginComponent",
        data() {
          return {
              username: '',
              password: '',
              re_password: '',
              email: ''
          }
        },
        methods: {
            submitLogin() {
                this.$emit('login', {
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