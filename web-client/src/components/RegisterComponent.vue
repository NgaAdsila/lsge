<template>
    <div class="register-form">
        <b-form @submit.stop.prevent="register">
            <p class="h4 text-center mb-4 register-label">{{ $t('register.label.title') }}</p>
            <b-form-group label-cols="2" label-cols-lg="2" label-size="sm"
                          :label="$t('common.label.name')"
                          label-for="input-name">
                <b-form-input id="input-name" size="sm"
                              v-model="$v.name.$model"
                              maxlength="40"
                              :state="validateState('name')"
                              aria-describedby="name-live-feedback"
                              ref="name"
                              class="grey-text"></b-form-input>
                <b-form-invalid-feedback id="name-live-feedback">
                    {{ $t('common.validation.required', { name: $t('common.label.name') }) }}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group label-cols="2" label-cols-lg="2" label-size="sm"
                          :label="$t('common.label.username')"
                          label-for="input-username">
                <b-form-input id="input-username" size="sm"
                              v-model="$v.username.$model"
                              maxlength="40"
                              :state="validateState('username')"
                              aria-describedby="username-live-feedback"
                              ref="username"
                              class="grey-text"></b-form-input>
                <b-form-invalid-feedback id="username-live-feedback">
                    {{ !$v.username.required
                        ? $t('common.validation.required', { name: $t('common.label.username') })
                        : (!$v.username.minLength || !$v.username.maxLength
                            ? $t('common.validation.min_max', { name: $t('common.label.username'), min: 4, max: 40 })
                            : $t('common.validation.strict_username', { name: $t('common.label.username') })) }}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group label-cols="2" label-cols-lg="2" label-size="sm"
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
            <b-form-group label-cols="2" label-cols-lg="2" label-size="sm"
                          :label="$t('register.label.re_password')"
                          label-for="input-re_password">
                <b-form-input type="password" id="input-re_password" size="sm"
                              v-model="$v.rePassword.$model"
                              maxlength="100"
                              :state="validateState('rePassword')"
                              aria-describedby="re_password-live-feedback"
                              ref="rePassword"
                              class="grey-text"></b-form-input>
                <b-form-invalid-feedback id="re_password-live-feedback">
                    {{ !$v.rePassword.required
                        ? $t('common.validation.required', { name: $t('register.label.re_password') })
                        : $t('register.validation.re_password.not_match') }}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group label-cols="2" label-cols-lg="2" label-size="sm"
                          :label="$t('common.label.email')"
                          label-for="input-email">
                <b-form-input type="email" id="input-email" size="sm"
                              v-model="$v.email.$model"
                              :state="validateState('email')"
                              aria-describedby="email-live-feedback"
                              ref="email"
                              class="grey-text"></b-form-input>
                <b-form-invalid-feedback id="email-live-feedback">
                    {{ !$v.email.required
                    ? $t('common.validation.required', { name: $t('common.label.email') })
                    : $t('common.validation.invalid_email') }}
                </b-form-invalid-feedback>
            </b-form-group>
            <div class="text-center mt-4">
                <b-button type="submit" variant="outline-primary">{{ $t('register.label.submit') }}</b-button>
            </div>
            <div class="text-center mt-4">
                {{ $t('register.label.login_link') }}
                <router-link to="/login">{{ $t('common.label.login') }}</router-link>!
            </div>
        </b-form>
    </div>
</template>

<script>
    import { validationMixin } from 'vuelidate';
    import { required, minLength, maxLength, email } from 'vuelidate/lib/validators';
    import { strictPassword, strictUserName, matchPassword } from '../plugins/vuevalidate';

    export default {
        name: "RegisterComponent",
        mixins: [validationMixin],
        data() {
            return {
                name: '',
                username: '',
                password: '',
                rePassword: '',
                email: ''
            }
        },
        validations: {
            name: {
                required
            },
            username: {
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
            },
            rePassword: {
                required,
                matchPassword
            },
            email: {
                required,
                email
            }
        },
        methods: {
            validateState(name) {
                const { $dirty, $error } = this.$v[name];
                return $dirty ? !$error : null;
            },
            register() {
                this.$v.$touch();
                if (this.$v.$anyError) {
                    this.focusFirstError();
                    return;
                }
                this.$emit('register', {
                    name: this.name,
                    username: this.username,
                    password: this.password,
                    email: this.email
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
.register-form {
    margin: 20vh auto;
    max-width: 700px;
    padding: 0.5rem 1rem;
    border: 1px solid lightgray;
    border-radius: 0.5rem;
    box-shadow: 5px 5px lightgray;

    .register-label {
        font-weight: bold;
    }
}
</style>