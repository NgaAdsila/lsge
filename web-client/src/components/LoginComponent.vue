<template>
    <div class="login-form">
        <b-form @submit.stop.prevent="onLogin">
            <p class="h4 text-center mb-4 login-label">{{ $t('login.label.title') }}</p>
            <b-form-group label-size="sm"
                          :label="$t('login.label.username')"
                          label-for="input-username">
                <b-form-input id="input-username" size="sm"
                              v-model="$v.usernameOrEmail.$model"
                              maxlength="40"
                              :state="validateState('usernameOrEmail')"
                              aria-describedby="name-live-feedback"
                              ref="usernameOrEmail"
                              class="grey-text"></b-form-input>
                <b-form-invalid-feedback id="name-live-feedback">
                    {{ !$v.usernameOrEmail.required
                        ? $t('common.validation.required', { name: $t('login.label.username') })
                        : (!$v.usernameOrEmail.minLength || !$v.usernameOrEmail.maxLength
                            ? $t('common.validation.min_max', { name: $t('login.label.username'), min: 4, max: 40 })
                            : $t('common.validation.strict_username', { name: $t('login.label.username') })) }}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group label-size="sm"
                          :label="$t('common.label.password')"
                          label-for="input-password">
                <b-form-input type="password" id="input-password" size="sm"
                              v-model="$v.password.$model"
                              maxlength="100"
                              :state="validateState('password')"
                              aria-describedby="password-live-feedback"
                              ref="password"
                              class="grey-text"></b-form-input>
                <b-form-invalid-feedback id="password-live-feedback">
                    {{ !$v.password.required
                    ? $t('common.validation.required', { name: $t('common.label.password') })
                    : (!$v.password.minLength || !$v.password.maxLength
                        ? $t('common.validation.min_max', { name: $t('common.label.password'), min: 8, max: 100 })
                        : $t('common.validation.strict_password')) }}
                </b-form-invalid-feedback>
            </b-form-group>
            <div class="text-center mt-4">
                <b-button type="submit" variant="outline-primary">{{ $t('login.label.submit') }}</b-button>
            </div>
            <div class="text-center mt-4">
                {{ $t('login.label.register_link') }}
                <router-link to="/register">{{ $t('common.label.register') }}</router-link>
                {{ $t('login.label.register_link2') }}
            </div>
        </b-form>
    </div>
</template>

<script>
    import { validationMixin } from 'vuelidate';
    import { required, minLength, maxLength } from 'vuelidate/lib/validators';
    import { strictPassword, strictUserName } from '../plugins/vuevalidate';

    export default {
        name: "LoginComponent",
        mixins: [validationMixin],
        data() {
          return {
              usernameOrEmail: '',
              password: ''
          }
        },
        validations: {
            usernameOrEmail: {
                required,
                minLength: minLength(4),
                maxLength: maxLength(40),
                strictUserName
            },
            password: {
                required,
                minLength: minLength(8),
                maxLength: maxLength(100),
                strictPassword
            }
        },
        methods: {
            validateState(name) {
                const { $dirty, $error } = this.$v[name];
                return $dirty ? !$error : null;
            },
            onLogin() {
                this.$v.$touch();
                if (this.$v.$anyError) {
                    this.focusFirstError();
                    return;
                }

                this.$emit('login', {
                    usernameOrEmail: this.usernameOrEmail,
                    password: this.password
                })
            },
            focusFirstError() {
                const invalidFields = Object.keys(this.$v.$params)
                    .filter(fn => this.$v[fn].$invalid);
                if (invalidFields) {
                    this.$refs[invalidFields[0]].$el.focus();
                }
            }
        }
    }
</script>

<style scoped>
.login-form {
    margin: 20vh auto;
    max-width: 700px;
    padding: 0.5rem 1rem;
    border: 1px solid lightgray;
    border-radius: 0.5rem;
    box-shadow: 5px 5px lightgray;

    .login-label {
        font-weight: bold;
    }
}
</style>